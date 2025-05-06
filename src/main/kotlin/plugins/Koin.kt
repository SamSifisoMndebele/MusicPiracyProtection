package ul.group14.plugins

import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.slf4jLogger
import ul.group14.repositories.AuthRepository
import ul.group14.database.connectToMongoDB

fun Application.configureKoin() {
    install(Koin) {
        slf4jLogger()
        modules(module {
            single(createdAtStart = true) { connectToMongoDB() }
            single(createdAtStart = true) { AuthRepository(get()) }
        })
    }
}
