package io.hugo.common.serialization

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.vladmihalcea.hibernate.type.util.ObjectMapperSupplier
import org.zalando.jackson.datatype.money.MoneyModule

class SpringObjectMapperSupplier : ObjectMapperSupplier {
    override fun get(): ObjectMapper {
        return INSTANCE
    }

    companion object {
        val INSTANCE: ObjectMapper = ObjectMapper()
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .registerModule(
                MoneyModule()
                    .withAmountFieldName("amount")
                    .withCurrencyFieldName("ccy")
                    .withFormattedFieldName("pretty")
            )
            .registerModule(JavaTimeModule())
            .findAndRegisterModules()
            .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)
            .enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .disable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS)
            .disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE)
            .disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
    }
}
