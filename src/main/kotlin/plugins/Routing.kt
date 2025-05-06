package ul.group14.plugins

import io.ktor.http.parameters
import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.html.respondHtml
import io.ktor.server.http.content.*
import io.ktor.server.request.receiveParameters
import io.ktor.server.resources.*
import io.ktor.server.response.respond
import io.ktor.server.response.respondRedirect
import io.ktor.server.routing.*
import io.ktor.server.sessions.get
import io.ktor.server.sessions.sessions
import io.ktor.server.sessions.set
import io.ktor.server.thymeleaf.Thymeleaf
import io.ktor.server.thymeleaf.ThymeleafContent
import org.koin.ktor.ext.inject
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import ul.group14.data.model.response.User
import ul.group14.pages.auth.profilePage
import ul.group14.repositories.AuthRepository
import kotlin.getValue

fun Application.configureRouting() {
    val authRepository by inject<AuthRepository>()
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
            get("/profile") {
                val principal = call.principal<User>() ?: run {
                    call.respondRedirect("/login", permanent = true)
                    return@get
                }
//                call.respond(ThymeleafContent(
//                    "profile",
//                    mapOf("user" to principal)
//                ))
                call.respondHtml {
                    profilePage(
                        names = principal.names,
                        email = principal.email,
                        photoUrl = principal.photoUrl
                    )
                }
            }

            post("/profile") {
                val principal = call.principal<User>() ?: run {
                    call.respondRedirect("/login", permanent = true)
                    return@post
                }
                val parameters = call.receiveParameters()
                val names = parameters["names"].toString()
                val email = parameters["email"].toString()

                try {
                    val user = authRepository.save(principal.email, names)
                    call.sessions.set(call.sessions.get<User>()?.copy(names = user.names) ?: user)
                    call.respondRedirect("/", permanent = true)
                } catch (e: Exception) {
                    e.printStackTrace()
                    call.respondRedirect("/", permanent = true)
                }
            }
        }
    }
}
