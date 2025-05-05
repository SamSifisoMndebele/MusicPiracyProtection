package ul.group14.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.hex
import org.koin.ktor.ext.inject
import ul.group14.data.model.response.User
import ul.group14.pages.auth.loginPage
import ul.group14.pages.auth.registerPage
import ul.group14.repositories.AuthRepository
import ul.group14.repositories.AuthRepository.Companion.PasswordException
import ul.group14.repositories.AuthRepository.Companion.UserNotFoundException
import kotlin.time.Duration.Companion.hours

fun Application.configureSecurity() {
    val authRepository by inject<AuthRepository>()
    install(Sessions) {
        val secretEncryptKey = hex("00112233445566778899aabbccddeeff")
        val secretSignKey = hex("6819b57a326945c1968f45236589")
        cookie<User>("user_session") {
            cookie.path = "/"
            cookie.secure = false
            cookie.maxAge = 8.hours
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretSignKey))
        }
    }
    authentication {
        session<User> {
            validate { session ->
                if(session.email.isNotBlank()) {
                    session
                } else {
                    null
                }
            }
            challenge {
                call.respondRedirect("/login")
            }
        }
    }
    routing {
        get("/login") {
            call.respondHtml {
                loginPage()
            }
        }
        post("/login") {
            val parameters = call.receiveParameters()
            val email = parameters["email"].toString()
            val password = parameters["password"].toString()
            try {
                val user = authRepository.login(email, password)
                call.sessions.set(user)
                println("$user logged in successfully")
                call.respondRedirect("/", permanent = true)
            } catch (e: UserNotFoundException) {
                call.respondHtml {
                    loginPage(
                        email = email,
                        password = password,
                        emailError = e.localizedMessage
                    )
                }
            } catch (e: PasswordException) {
                call.respondHtml {
                    loginPage(
                        email = email,
                        password = password,
                        passwordError = e.localizedMessage
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respondHtml {
                    loginPage(
                        email = email,
                        password = password,
                    )
                }
            }
        }

        get("/register") {
            call.respondHtml {
                registerPage()
            }
        }
        post("/register") {
            val parameters = call.receiveParameters()
            val email = parameters["email"].toString()
            val password = parameters["password"].toString()
            val passwordConfirm = parameters["password_confirm"].toString()
            if (password != passwordConfirm) {
                call.respondHtml {
                    registerPage(
                        email = email,
                        password = password,
                        passwordConfirm = passwordConfirm,
                        passwordConfirmError = "Passwords do not match."
                    )
                }
            }
            call.respond("Register Response")
        }
    }
}
