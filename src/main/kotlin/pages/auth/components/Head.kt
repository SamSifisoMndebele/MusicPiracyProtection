package ul.group14.pages.auth.components

import kotlinx.html.*


@HtmlTagMarker
inline fun HTML.headTag(
    title : String,
    crossinline block : HEAD.() -> Unit = {}
) {
    HEAD(emptyMap, consumer).visit {
        title { +title }
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
        block()
    }
}