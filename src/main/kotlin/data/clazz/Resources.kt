package ul.group14.data.clazz

import io.ktor.resources.*

@Resource("/articles")
class Articles(val sort: String? = "new")
