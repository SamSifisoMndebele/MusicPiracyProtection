package ul.group14.pages.auth

import kotlinx.html.*
import ul.group14.pages.auth.components.headTag
import ul.group14.pages.auth.components.scripts

fun HTML.registerPage(
    email: String = "",
    password: String = "",
    passwordConfirm: String = "",
    emailError: String? = null,
    passwordError: String? = null,
    passwordConfirmError: String? = null,
) {
    headTag(title = "Register - Music Piracy Protection System")
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
                        action = "/register",
                        method = FormMethod.post,
                        classes = "login100-form validate-form"
                    ) {
                        span("login100-form-title") { +"Register" }
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
                        div("wrap-input100 validate-input") {
                            if (passwordConfirmError.isNullOrBlank()) {
                                attributes["data-validate"] = "Password is required"
                            } else {
                                attributes["data-validate"] = passwordConfirmError
                                classes += "alert-validate"
                            }
                            input(type = InputType.password, classes = "input100") {
                                name = "password_confirm"
                                placeholder = "Confirm Password"
                                value = passwordConfirm
                            }
                            span("focus-input100")
                            span("symbol-input100") {
                                i("fa fa-lock") { attributes["aria-hidden"] = "true" }
                            }
                        }
                        div("container-login100-form-btn") {
                            button(type = ButtonType.submit, classes = "login100-form-btn") {
                                +"Register"
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

        scripts()
//        script { src = "/js/register.js" }
    }
}