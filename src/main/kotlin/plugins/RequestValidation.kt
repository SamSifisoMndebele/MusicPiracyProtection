package ul.group14.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import ul.group14.data.model.request.UserReq

fun Application.configureRequestValidation() {
    install(RequestValidation) {
        validate<UserReq> { bodyText ->
            when {
                bodyText.name.isBlank() -> ValidationResult.Invalid("Name can't be empty")
                else -> ValidationResult.Valid
            }
        }
    }
//    install(StatusPages) {
//        exception<Throwable> { call, cause ->
//            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
//        }
//    }
}
