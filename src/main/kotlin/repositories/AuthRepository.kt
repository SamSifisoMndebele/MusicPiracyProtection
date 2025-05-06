package ul.group14.repositories

import com.mongodb.MongoWriteException
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import ul.group14.data.model.domain.UserDb
import ul.group14.data.model.request.UserReq
import ul.group14.data.model.response.User
import ul.group14.database.databaseQuery
import utils.PasswordUtils

class AuthRepository(private val database: MongoDatabase) {
    private var usersCollection: MongoCollection<UserDb>
    init {
        runBlocking {
            database.createCollection("users")
            usersCollection = database.getCollection("users")
            usersCollection.createIndex(
                keys = Indexes.ascending(UserDb::email.name),
                options = IndexOptions().unique(true)
            )
        }
    }

    companion object {
        class UserNotFoundException(message: String = "User is not found.") : Exception(message)
        class UserAlreadyExistsException(message: String = "User with this email already exists.") : Exception(message)
        class PasswordException(message: String = "Invalid Password.") : Exception(message)
    }

    suspend fun register(names: String, email: String, password: String) = databaseQuery {
        val user = UserDb(email = email, names = names, passwordHash = PasswordUtils.encryptPassword(password))
        try {
            usersCollection.insertOne(user)
            user.toUser()
        } catch (e: MongoWriteException) {
            e.printStackTrace()
            println(user)
            e.code == 11000 && throw UserAlreadyExistsException()
            throw e
        }
    }

    suspend fun login(email: String, password: String) = databaseQuery {
        val user = usersCollection.find(eq(UserDb::email.name, email))
            .firstOrNull()
            ?: throw UserNotFoundException()
        when {
            PasswordUtils.validatePassword(password, user.passwordHash) -> user.toUser()
            else -> throw PasswordException()
        }
    }

    suspend fun save(email: String, names: String) = databaseQuery {
        val user = usersCollection.findOneAndUpdate(
            filter = eq(UserDb::email.name, email),
            update = Updates.set(UserDb::names.name, names)
        ) ?: throw UserNotFoundException()
        user.toUser()
    }
}