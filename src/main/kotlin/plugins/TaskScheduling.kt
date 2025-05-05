package ul.group14.plugins

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.kotlin.client.coroutine.MongoClient
import io.github.flaxoos.ktor.server.plugins.taskscheduling.TaskScheduling
import io.github.flaxoos.ktor.server.plugins.taskscheduling.managers.lock.database.mongoDb
import io.ktor.server.application.*

fun Application.configureTaskScheduling() {
    install(TaskScheduling) {
        mongoDb("my mongodb manager") {
            databaseName = "test"
            val connectionString = "mongodb+srv://samsmndebele:ITcjZuyZydNqF7nd@musicpiracyprotectioncl.3tlem0g.mongodb.net/" +
                    "?retryWrites=true&maxPoolSize=20&w=majority&appName=MusicPiracyProtectionCluster"
            val serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build()
            val mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(ConnectionString(connectionString))
                .serverApi(serverApi)
                .build()
            client = MongoClient.create(mongoClientSettings)
        }

        task(taskManagerName = "my mongodb manager") { // if no taskManagerName is provided, the task would be assigned to the default manager
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
