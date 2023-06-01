package commandArguments

import kotlinx.serialization.Serializable

@Serializable
data class CommandArgument(val name: String, val type: String, var value: String? = null)
