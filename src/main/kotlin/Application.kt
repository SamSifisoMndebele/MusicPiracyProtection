package ul.group14

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.netty.EngineMain.createServer

fun main(args: Array<String>) {
    createServer(args).start(wait = true)
}

fun Application.module() {
    configureHTTP()
//    configureSecurity()
    configureSerialization()
    configureDatabases()
//    configureTemplating()
//    configureFrameworks()
//    configureAdministration()
//    configureRouting()
}
