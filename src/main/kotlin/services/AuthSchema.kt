package ul.group14.services

import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.bson.Document
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class User(
    @BsonId @Contextual
    val id: ObjectId,
    val email: String,
    val name: String,
    val lastname: String,
    val picture: String?,
    val passwordHash: String,
    val role: String,
    val isEmailVerified: Boolean = false,

    ) {
    fun toDocument(): Document = Document.parse(Json.encodeToString(this))

    companion object {
        private val json = Json { ignoreUnknownKeys = true; prettyPrint = true }
        fun Document.toUser(): User = json.decodeFromString(toJson())
    }
}


class AuthService(private val database: MongoDatabase) {
    var usersCollection: MongoCollection<Document>
    init {
        runBlocking {
            database.createCollection("users")
            usersCollection = database.getCollection("users")
        }
    }


    suspend fun createUser(user: User): String = withContext(Dispatchers.IO) {
        val doc = user.toDocument()
        usersCollection.insertOne(doc)
        doc["id"].toString()
    }
}