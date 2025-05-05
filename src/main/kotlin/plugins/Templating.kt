package ul.group14.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.html.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.thymeleaf.*
import kotlinx.html.*
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver
import ul.group14.data.model.response.User

fun Application.configureTemplating() {
    install(Thymeleaf) {
        setTemplateResolver(ClassLoaderTemplateResolver().apply {
            prefix = "thymeleaf/"
            suffix = ".html"
            characterEncoding = "utf-8"
        })
    }
    routing {
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