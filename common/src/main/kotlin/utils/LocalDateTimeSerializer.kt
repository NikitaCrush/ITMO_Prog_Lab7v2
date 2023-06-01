package utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Serializer for LocalDateTime objects.
 */
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    /**
     * Serializes a LocalDateTime object into a string.
     *
     * @param encoder The encoder used to encode the LocalDateTime object.
     * @param value The LocalDateTime object to serialize.
     */
    override fun serialize(encoder: Encoder, value: LocalDateTime) {
        val string = value.format(formatter)
        encoder.encodeString(string)
    }

    /**
     * Deserializes a LocalDateTime object from a string.
     *
     * @param decoder The decoder used to decode the LocalDateTime object.
     * @return The deserialized LocalDateTime object.
     */
    override fun deserialize(decoder: Decoder): LocalDateTime {
        val string = decoder.decodeString()
        return LocalDateTime.parse(string, formatter)
    }
}
