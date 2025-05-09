package ul.group14.database

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
 * Constructs a MongoDB connection string based on configuration parameters found in the application's environment.
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
 * @return A `ConnectionString` instance representing the MongoDB connection URL based on the configuration.
 */
fun Application.connectionString(): ConnectionString {
    val user = environment.config.tryGetString("mongoDb.user") ?: throw Exception("Missing mongoDb.user property")
    val password = environment.config.tryGetString("mongoDb.password") ?: throw Exception("Missing mongoDb.password property")
    val host = environment.config.tryGetString("mongoDb.host") ?: throw Exception("Missing mongoDb.host property")
    val port = environment.config.tryGetString("mongoDb.port")
    val maxPoolSize = environment.config.tryGetString("mongoDb.maxPoolSize")?.toInt() ?: 20
    val clusterName = environment.config.tryGetString("mongoDb.clusterName") ?: "MusicPiracyProtectionCluster"

    val url = port?.let { portVal -> "$host:$portVal" } ?: host
    val parameters = "?retryWrites=true&maxPoolSize=$maxPoolSize&w=majority&appName=$clusterName"
    return ConnectionString("mongodb+srv://$user:$password@$url/$parameters")
}

/**
 * Establishes connection with a MongoDB database.
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
    val databaseName = environment.config.tryGetString("mongoDb.databaseName") ?: "test"
    val connectionString = connectionString()

    val settings = MongoClientSettings.builder()
        .applyConnectionString(connectionString)
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