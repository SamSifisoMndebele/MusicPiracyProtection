package ul.group14.pages.auth

import kotlinx.html.*
import ul.group14.pages.auth.components.headTag
import ul.group14.pages.auth.components.scripts

fun HTML.profilePage(
    names: String = "",
    email: String = "",
    photoUrl: String? = null,
) {
    headTag(title = "Profile - $names")
    body {
        div("limiter") {
            div("container-login100") {
                div("wrap-login100") {
                    div("login100-pic js-tilt") {
                        attributes["data-tilt"] = "true"
                        h1("login100-pic-title") { +"Profile" }
                        img(src = photoUrl, alt = "IMG", classes = "img-profile")
                    }
                    form(
                        action = "/profile",
                        method = FormMethod.post,
                        classes = "login100-form validate-form"
                    ) {
                        div("wrap-input100 validate-input") {
                            input(type = InputType.email, classes = "input100") {
                                name = "email"
                                placeholder = "Email"
                                value = email
                                disabled = true
                            }
                            span("focus-input100")
                            span("symbol-input100") {
                                i("fa fa-envelope") { attributes["aria-hidden"] = "true" }
                            }
                        }
                        div("wrap-input100 validate-input") {
                            attributes["data-validate"] = "Your names are required"
                            input(type = InputType.text, classes = "input100") {
                                name = "names"
                                placeholder = "Full Names"
                                value = names
                            }
                            span("focus-input100")
                            span("symbol-input100") {
                                i("fa fa-user-circle") { attributes["aria-hidden"] = "true" }
                            }
                        }
                        div("container-login100-form-btn") {
                            button(type = ButtonType.submit, classes = "login100-form-btn") {
                                +"Save Profile"
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