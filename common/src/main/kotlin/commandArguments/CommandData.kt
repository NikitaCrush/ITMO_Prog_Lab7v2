package commandArguments

import kotlinx.serialization.Serializable

/**
 * Data class for a command data.
 *
 * @property commandName The name of the command.
 * @property arguments A list of arguments for the command, it can be empty.
 * @property token A string representing the token for the user who executed the command, it is optional and can be null.
 */
@Serializable
data class CommandData(
    val commandName: String,
    val arguments: List<CommandArgument> = emptyList(),
    var token: String? = null
)
