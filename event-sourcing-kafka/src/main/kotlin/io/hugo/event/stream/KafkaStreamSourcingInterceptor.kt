package io.hugo.event.stream

import io.hugo.event.blocking.dal.EventRepo
import org.slf4j.LoggerFactory
import org.springframework.cloud.stream.messaging.DirectWithAttributesChannel
import org.springframework.integration.config.GlobalChannelInterceptor
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Component

/**
 * This interceptor is used to capture the message before it is sent to the channel.
 * It is used to capture the message and save it to the database.
 *
 * This does not really work because the message is not yet sent to the channel,
 * so this interceptor will not be able to capture the destination topic.
 *
 * For the stream based Kafka messaging, place configure the ProducerInterceptor instead of using this.
 */
@Component
@GlobalChannelInterceptor
class KafkaStreamSourcingInterceptor(
    private val eventRepo: EventRepo,
    private val messageConverter: ChannelMessageConverter
) : ChannelInterceptor {

    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        try  {
            // This preSend will be invoked twice for each message based on testing.
            // We only need to capture it into database once.
            if (channel is DirectWithAttributesChannel) {
                val entity = messageConverter.convert(message)
                eventRepo.save(entity)
            }
        } catch (e: Exception) {
            logger.error("Failed to capture message ${message.headers.id} because ${e.message}", e)
        }
        return message
    }

    private val logger = LoggerFactory.getLogger(KafkaStreamSourcingInterceptor::class.java)
}
