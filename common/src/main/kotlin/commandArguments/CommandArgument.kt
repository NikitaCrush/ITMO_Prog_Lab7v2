package commandArguments

import kotlinx.serialization.Serializable

/**
 * Data class for command arguments.
 *
 * @property name The name of the command argument.
 * @property type The type of the command argument.
 * @property value The value of the command argument, it is optional and can be null.
 */
@Serializable
data class CommandArgument(
    val name: String,
    val type: String,
    var value: String? = null
)
