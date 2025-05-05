package ul.group14

import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain.createServer
import ul.group14.plugins.*

fun main(args: Array<String>) {
    createServer(args).start(wait = true)
}

fun Application.module() {
    configureHTTP()
    configureSerialization()
    configureKoin()
    configureSecurity()
    configureRequestValidation()
    configureTemplating()
//    configureTaskScheduling()
    configureRouting()
}
