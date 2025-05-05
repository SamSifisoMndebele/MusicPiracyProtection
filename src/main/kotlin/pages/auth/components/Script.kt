package ul.group14.pages.auth.components

import kotlinx.html.FlowOrMetaDataOrPhrasingContent
import kotlinx.html.HtmlTagMarker
import kotlinx.html.script

@HtmlTagMarker
fun FlowOrMetaDataOrPhrasingContent.scripts() {
    script { src = "/vendor/jquery/jquery-3.2.1.min.js" }
    script { src = "/vendor/bootstrap/js/popper.js" }
    script { src = "/vendor/bootstrap/js/bootstrap.min.js" }
    script { src = "/vendor/select2/select2.min.js" }
    script { src = "/vendor/tilt/tilt.jquery.min.js" }
    script { +"$('.js-tilt').tilt({scale: 1.1})" }
    script { src = "/js/auth.js" }
}