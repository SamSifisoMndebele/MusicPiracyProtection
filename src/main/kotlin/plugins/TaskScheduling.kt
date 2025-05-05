package ul.group14.plugins

import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import io.github.flaxoos.ktor.server.plugins.taskscheduling.TaskScheduling
import io.github.flaxoos.ktor.server.plugins.taskscheduling.managers.lock.database.mongoDb
import io.ktor.server.application.*
import io.ktor.server.config.tryGetString
import ul.group14.repositories.connectionString
import java.util.concurrent.TimeUnit

fun Application.configureTaskScheduling() {
    install(TaskScheduling) {
        mongoDb("mongodb manager") {
            databaseName = environment.config.tryGetString("mongoDb.databaseName") ?: "test"
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
            client = MongoClient.create(settings)
        }

        task(taskManagerName = "mongodb manager") {
            name = "My task"
            task = { taskExecutionTime ->
                log.info("My task is running: $taskExecutionTime")
            }
            kronSchedule = {
                minutes {
                    from(0).every(5)
                }
            }
            concurrency = 1
        }
    }
}
