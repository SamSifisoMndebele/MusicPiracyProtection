package ul.group14.repositories

import com.mongodb.MongoWriteException
import com.mongodb.client.model.Filters.eq
import com.mongodb.client.model.IndexOptions
import com.mongodb.client.model.Indexes
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import ul.group14.data.model.domain.UserDb
import ul.group14.data.model.request.UserReq
import ul.group14.data.model.response.User
import utils.PasswordUtils

class AuthRepository(private val database: MongoDatabase) {
    private var usersCollection: MongoCollection<UserDb>
    init {
        runBlocking {
            database.createCollection("users")
            usersCollection = database.getCollection("users")
            usersCollection.createIndex(
                keys = Indexes.text(UserDb::email.name),
                options = IndexOptions().unique(true)
            )
        }
    }

    companion object {
        class UserNotFoundException(message: String = "User is not found.") : Exception(message)
        class UserAlreadyExistsException(message: String = "User already exists") : Exception(message)
        class PasswordException(message: String = "Invalid Password.") : Exception(message)
        class InvalidEmailException(message: String = "Invalid Email") : Exception(message)
    }

    suspend fun createUser(user: UserReq): String = databaseQuery {
        val result = try {
            usersCollection.insertOne(user.toUserDb())
        } catch (e: MongoWriteException) {
            e.code == 11000 && throw UserAlreadyExistsException()
            throw e
        }
        result.wasAcknowledged() || throw UserAlreadyExistsException()
        result.insertedId?.toString().toString()
    }

    suspend fun readUserById(id: String): User = databaseQuery {
        usersCollection.find(eq("_id", ObjectId(id)))
            .firstOrNull()
            ?.toUser()
            ?: throw UserNotFoundException()
    }

//    suspend fun readUserByEmail(email: String): User = databaseQuery {
//        usersCollection.find(eq(UserDb::email.name, email))
//            .firstOrNull()
//            ?.toUser()
//            ?: throw NoUserFoundException()
//    }

//    suspend fun updateUser(id: String, user: UserReq): UserDb? = databaseQuery {
//        usersCollection.findOneAndReplace(Filters.eq("_id", ObjectId(id)), user.toUserDb())
//    }

    suspend fun deleteUser(id: String): User? = databaseQuery {
        usersCollection.findOneAndDelete(eq("_id", ObjectId(id)))
            ?.toUser()
    }

    suspend fun validatePassword(email: String, password: String) = databaseQuery {
        val user = usersCollection.find(eq(UserDb::email.name, email)).firstOrNull()
        if (user == null) return@databaseQuery false
        PasswordUtils.validatePassword(password, user.passwordHash)
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
}