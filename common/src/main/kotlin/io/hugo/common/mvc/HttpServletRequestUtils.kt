package io.hugo.common.mvc

import org.springframework.http.HttpHeaders
import javax.servlet.http.HttpServletRequest

object HttpServletRequestUtils {
    fun HttpServletRequest.readHeaders() =
        headerNames.asSequence().fold(HttpHeaders()) { headers, name ->
            headers.apply {
                addAll(name, getHeaders(name).toList())
            }
        }
}