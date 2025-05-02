package ul.group14

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoDatabase
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.runBlocking
import org.bson.Document

fun Application.configureDatabases() {
    val mongoDatabase = connectToMongoDB()
    val carService = CarService(mongoDatabase)
    routing {
        // Create car
        post("/cars") {
            val car = call.receive<Car>()
            val id = carService.create(car)
            call.respond(HttpStatusCode.Created, id)
        }
        // Read car
        get("/cars/{id}") {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("No ID found")
            carService.read(id)?.let { car ->
                call.respond(car)
            } ?: call.respond(HttpStatusCode.NotFound)
        }
        // Update car
        put("/cars/{id}") {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("No ID found")
            val car = call.receive<Car>()
            carService.update(id, car)?.let {
                call.respond(HttpStatusCode.OK)
            } ?: call.respond(HttpStatusCode.NotFound)
        }
        // Delete car
        delete("/cars/{id}") {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("No ID found")
            carService.delete(id)?.let {
                call.respond(HttpStatusCode.OK)
            } ?: call.respond(HttpStatusCode.NotFound)
        }
    }
}

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
fun Application.connectToMongoDB(): MongoDatabase {
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
    val serverApi = ServerApi.builder()
        .version(ServerApiVersion.V1)
        .build()
    val mongoClientSettings = MongoClientSettings.builder()
        .applyConnectionString(ConnectionString(connectionString))
        .serverApi(serverApi)
        .build()
    val mongoClient = MongoClients.create(mongoClientSettings)
    val database = mongoClient.getDatabase(databaseName)
    monitor.subscribe(ApplicationStopped) {
        mongoClient.close()
    }
    return database
}
