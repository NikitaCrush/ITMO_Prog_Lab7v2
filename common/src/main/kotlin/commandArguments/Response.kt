package commandArguments

import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val success: Boolean,
    val message: String
)
