package ul.group14.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.http.content.*
import io.ktor.server.resources.*
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.Thymeleaf
import io.ktor.server.thymeleaf.ThymeleafContent
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import ul.group14.data.model.response.User

fun Application.configureRouting() {
    install(Resources)
    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver().apply {
            prefix = "thymeleaf/"
            suffix = ".html"
            characterEncoding = "utf-8"
        })
    }
    routing {
        staticResources("/", "static")
        authenticate {
            get {
                val principal = call.principal<User>() ?: run {
                    call.respondRedirect("/login", permanent = true)
                    return@get
                }
                call.respond(ThymeleafContent(
                    "index",
                    mapOf("user" to principal)
                ))
            }
        }
    }
}
