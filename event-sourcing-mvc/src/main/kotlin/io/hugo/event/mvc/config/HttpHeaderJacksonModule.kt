package io.hugo.event.mvc.config

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.http.HttpHeaders
import org.springframework.util.MultiValueMap

class HttpHeaderJacksonModule : SimpleModule("HttpHeaders",
    Version(0, 0, 1, null, "io.hugo", "common-jackson-headers")
) {
    init {
        addAbstractTypeMapping(MultiValueMap::class.java, HttpHeaders::class.java)
    }
}
