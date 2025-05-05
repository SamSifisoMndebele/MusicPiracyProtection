package ul.group14.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.html.*
import io.ktor.server.request.receiveParameters
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.util.hex
import kotlinx.html.ButtonType
import kotlinx.html.FormMethod
import kotlinx.html.InputType
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.i
import kotlinx.html.img
import kotlinx.html.input
import kotlinx.html.link
import kotlinx.html.meta
import kotlinx.html.script
import kotlinx.html.span
import kotlinx.html.title
import org.koin.ktor.ext.inject
import ul.group14.data.model.response.User
import ul.group14.pages.auth.loginPage
import ul.group14.pages.auth.registerPage
import ul.group14.repositories.AuthRepository
import ul.group14.repositories.AuthRepository.Companion.PasswordException
import ul.group14.repositories.AuthRepository.Companion.UserAlreadyExistsException
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
            val names = parameters["names"].toString()
            val password = parameters["password"].toString()
            val passwordConfirm = parameters["password_confirm"].toString()
            if (password != passwordConfirm) {
                call.respondHtml {
                    registerPage(
                        names = names,
                        email = email,
                        password = password,
                        passwordConfirm = passwordConfirm,
                        passwordConfirmError = "Passwords do not match."
                    )
                }
            }
            try {
                val user = authRepository.register(names, email, password)
                call.sessions.set(user)
                call.respondRedirect("/", permanent = true)
            } catch (e: UserAlreadyExistsException) {
                call.respondHtml {
                    registerPage(
                        names = names,
                        email = email,
                        password = password,
                        passwordConfirm = passwordConfirm,
                        emailError = e.localizedMessage
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                call.respondHtml {
                    registerPage(
                        names = names,
                        email = email,
                        password = password,
                        passwordConfirm = passwordConfirm,
                    )
                }
            }
        }

        get("/logout") {
            call.sessions.clear<User>()
            call.respondRedirect("/login", permanent = true)
        }

        authenticate {
            get("/user") {
                val user = call.sessions.get<User>()
                if (user == null) {
                    call.respondRedirect("/login", permanent = true)
                }
            }
        }

        get("/password") {
            call.respondHtml {
                head {
                    title { +"Password Reset" }
                    meta { charset = "UTF-8" }
                    meta { name = "viewport"; content = "width=device-width, initial-scale=1" }
                    link(rel = "stylesheet", href = "/vendor/bootstrap/css/bootstrap.min.css", type = "text/css")
                    link(
                        rel = "stylesheet",
                        href = "/fonts/font-awesome-4.7.0/css/font-awesome.min.css",
                        type = "text/css"
                    )
                    link(rel = "stylesheet", href = "/vendor/animate/animate.css", type = "text/css")
                    link(
                        rel = "stylesheet",
                        href = "/vendor/css-hamburgers/hamburgers.min.css",
                        type = "text/css"
                    )
                    link(rel = "stylesheet", href = "/vendor/select2/select2.min.css", type = "text/css")
                    link(rel = "stylesheet", href = "/css/util.css", type = "text/css")
                    link(rel = "stylesheet", href = "/css/auth.css", type = "text/css")
                }
                body {
                    div("limiter") {
                        div("container-login100") {
                            div("wrap-login100") {
                                div("login100-pic js-tilt") {
                                    attributes["data-tilt"] = "true"
                                    h1("login100-pic-title") { +"Music Piracy Protection System" }
                                    img(src = "/images/background.JPG", alt = "IMG")
                                }
                                form(
                                    action = "/password",
                                    method = FormMethod.post,
                                    classes = "login100-form validate-form"
                                ) {
                                    span("login100-form-title") { +"Password Reset" }
                                    div("wrap-input100 validate-input") {
                                        attributes["data-validate"] = "Valid email is required: ex@abc.xyz"
                                        input(type = InputType.email, classes = "input100") {
                                            name = "email"
                                            placeholder = "Email"
                                        }
                                        span("focus-input100")
                                        span("symbol-input100") {
                                            i("fa fa-envelope") { attributes["aria-hidden"] = "true" }
                                        }
                                    }
                                    div("container-login100-form-btn") {
                                        button(type = ButtonType.submit, classes = "login100-form-btn") {
                                            +"Send Reset Email"
                                        }
                                    }
                                    div("text-center p-t-16") {
                                        a("/login", classes = "txt2") {
                                            +"Login to your Account"
                                            i("fa fa-long-arrow-right m-l-5") { attributes["aria-hidden"] = "true" }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    script { src = "/vendor/jquery/jquery-3.2.1.min.js" }
                    script { src = "/vendor/bootstrap/js/popper.js" }
                    script { src = "/vendor/bootstrap/js/bootstrap.min.js" }
                    script { src = "/vendor/select2/select2.min.js" }
                    script { src = "/vendor/tilt/tilt.jquery.min.js" }
                    script { +"$('.js-tilt').tilt({scale: 1.1})" }
                    script { src = "/js/password.js" }
                }
            }
        }
    }
}
