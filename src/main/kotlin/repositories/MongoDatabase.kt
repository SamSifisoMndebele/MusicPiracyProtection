package ul.group14.repositories

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import io.ktor.server.application.*
import io.ktor.server.config.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

/**
 * Establishes connection with a MongoDB database.
 *
 * The following configuration properties (in application.yaml/application.conf) can be specified:
 * * `mongoDb.user` username for your database
 * * `mongoDb.password` password for the user
 * * `mongoDb.host` host that will be used for the database connection
 * * `mongoDb.port` port that will be used for the database connection
 * * `mongoDb.maxPoolSize` maximum number of connections to a MongoDB server
 * * `mongoDb.databaseName` name of the database
 * * `mongoDb.clusterName` name of the cluster
 *
 * IMPORTANT NOTE: to make MongoDB connection working, you have to start a MongoDB server first.
 * See the instructions here: https://www.mongodb.com/docs/manual/administration/install-community/
 * all the parameters above
 *
 * @returns [MongoDatabase] instance
 * */
private var database: MongoDatabase? = null
fun Application.connectToMongoDB(): MongoDatabase {
    if (database != null) return database!!
    val user = environment.config.tryGetString("mongoDb.user")
    val password = environment.config.tryGetString("mongoDb.password")
    val host = environment.config.tryGetString("mongoDb.host") ?: "127.0.0.1"
    val port = environment.config.tryGetString("mongoDb.port")
    val maxPoolSize = environment.config.tryGetString("mongoDb.maxPoolSize")?.toInt() ?: 20
    val databaseName = environment.config.tryGetString("mongoDb.databaseName") ?: "test"
    val clusterName = environment.config.tryGetString("mongoDb.clusterName") ?: "MusicPiracyProtectionCluster"

    val credentials = user?.let { userVal -> password?.let { passwordVal -> "$userVal:$passwordVal@" } }.orEmpty()
    val url = port?.let { portVal -> "$host:$portVal" } ?: host
    val parameters = "?retryWrites=true&maxPoolSize=$maxPoolSize&w=majority&appName=$clusterName"
    val connectionString = "mongodb+srv://$credentials$url/$parameters"

    val settings = MongoClientSettings.builder()
        .applyConnectionString(ConnectionString(connectionString))
        .applyToSocketSettings { builder ->
            builder.connectTimeout(20, TimeUnit.SECONDS)
        }
        .serverApi(ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build())
        .build()
    val mongoClient = MongoClient.create(settings)
    database = mongoClient.getDatabase(databaseName)
    monitor.subscribe(ApplicationStopped) {
        mongoClient.close()
    }
    return database!!
}

suspend inline fun <T> databaseQuery(
    noinline block: suspend CoroutineScope.() -> T
): T = withContext(Dispatchers.IO, block)