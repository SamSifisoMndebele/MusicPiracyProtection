package ul.group14.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.request.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ul.group14.data.model.request.UserReq
import ul.group14.repositories.AuthRepository
import java.nio.file.attribute.UserPrincipalNotFoundException

fun Application.configureRouting() {
    val authRepository by inject<AuthRepository>()
    install(Resources)
    routing {
        staticResources("/", "static")

        // Create user
        post("/users") {
            try {
                val user = call.receive<UserReq>()
                val id = authRepository.createUser(user)
                call.respond(HttpStatusCode.Created, id)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Failed to create user: $e")
                e.printStackTrace()
            }
        }
        // Read user
        get("/users/{id}") {
            try {
                val id = call.parameters["id"] ?: throw IllegalArgumentException("No ID found")
                val user = authRepository.readUserById(id)
                call.respond(user)
            } catch (e: UserPrincipalNotFoundException) {
                call.respond(HttpStatusCode.NotFound, e.localizedMessage)
                e.printStackTrace()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, "Failed to get user: $e")
                e.printStackTrace()
            }
        }
        // Update user
//        put("/users/{id}") {
//            val id = call.parameters["id"] ?: throw IllegalArgumentException("No ID found")
//            val user = call.receive<UserReq>()
//            authRepository.updateUser(id, user)?.let {
//                call.respond(HttpStatusCode.OK)
//            } ?: call.respond(HttpStatusCode.NotFound)
//        }
        // Delete user
        delete("/users/{id}") {
            val id = call.parameters["id"] ?: throw IllegalArgumentException("No ID found")
            authRepository.deleteUser(id)?.let {
                call.respond(HttpStatusCode.OK)
            } ?: call.respond(HttpStatusCode.NotFound)
        }
    }
}
