package ul.group14.pages.auth

import kotlinx.html.ButtonType
import kotlinx.html.FormMethod
import kotlinx.html.HTML
import kotlinx.html.InputType
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.button
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.i
import kotlinx.html.id
import kotlinx.html.img
import kotlinx.html.input
import kotlinx.html.script
import kotlinx.html.span
import ul.group14.pages.auth.components.headTag
import ul.group14.pages.auth.components.scripts

fun HTML.loginPage(
    email: String = "",
    password: String = "",
    emailError: String? = null,
    passwordError: String? = null,
) {
    headTag(title = "Login - Music Piracy Protection System")
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
                        action = "/login",
                        method = FormMethod.post,
                        classes = "login100-form validate-form"
                    ) {
                        id = "login-form"
                        span("login100-form-title") { +"Login" }
                        div("wrap-input100 validate-input") {
                            if (emailError.isNullOrBlank()) {
                                attributes["data-validate"] = "Valid email is required: email@mail.com"
                            } else {
                                attributes["data-validate"] = emailError
                                classes += "alert-validate"
                            }
                            input(type = InputType.email, classes = "input100") {
                                name = "email"
                                placeholder = "Email"
                                value = email
                            }
                            span("focus-input100")
                            span("symbol-input100") {
                                i("fa fa-envelope") { attributes["aria-hidden"] = "true" }
                            }
                        }
                        div("wrap-input100 validate-input") {
                            if (passwordError.isNullOrBlank()) {
                                attributes["data-validate"] = "Password is required"
                            } else {
                                attributes["data-validate"] = passwordError
                                classes += "alert-validate"
                            }
                            input(type = InputType.password, classes = "input100") {
                                name = "password"
                                placeholder = "Password"
                                value = password
                            }
                            span("focus-input100")
                            span("symbol-input100") {
                                i("fa fa-lock") { attributes["aria-hidden"] = "true" }
                            }
                        }
                        div("container-login100-form-btn") {
                            button(type = ButtonType.submit, classes = "login100-form-btn button-login") {
                                +"Login"
                            }
                        }
                        div("text-center p-t-16") {
                            a("/password", classes = "txt2") {
                                +"Forgot Password"
                                i("fa fa-long-arrow-right m-l-5") { attributes["aria-hidden"] = "true" }
                            }
                        }
                        div("text-center p-t-16") {
                            a("/register", classes = "txt2") {
                                +"Create your Account"
                                i("fa fa-long-arrow-right m-l-5") { attributes["aria-hidden"] = "true" }
                            }
                        }
                    }
                }
            }
        }
        scripts()
    }
}