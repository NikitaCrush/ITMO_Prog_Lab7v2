package commandArguments

import kotlinx.serialization.Serializable

/**
 * Data class for a response.
 *
 * @property success A boolean that indicates whether the request was successful.
 * @property message A string that contains the message for the response.
 */
@Serializable
data class Response(
    val success: Boolean,
    val message: String
)
