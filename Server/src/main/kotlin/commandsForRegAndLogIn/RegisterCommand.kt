package commandsForRegAndLogIn

import commandArguments.CommandArgument
import commandArguments.CommandType
import commands.Command
import data.Messages
import data.User
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * The RegisterCommand class handles the process of user registration.
 *
 * @property userCollection The collection of users where the new user's credentials will be stored.
 */
class RegisterCommand : Command() {
    override val commandType = CommandType.USER_REGISTRATION
    override val commandArgs = listOf(CommandArgument("reg", "User"))

    /**
     * Attempts to register a new user with the provided credentials.
     *
     * @param args A list of arguments where the first element should be the JSON representation of a User.
     * @param token An optional token for current logged in user.
     * @return A String message indicating the result of the registration attempt.
     */
    override fun execute(args: List<Any>, token: String?): String {
        if (args.size != 1) {
            throw IllegalArgumentException("Register command expects 1 argument, but got ${args.size}.")
        }

        val user = args[0] as? String
            ?: throw IllegalArgumentException("Argument for register command is not of type String.")

        val deserializedUser: User = Json.decodeFromString(user)

        return if(userCollection.register(deserializedUser.username, deserializedUser.password)) {
            Messages.REGISTRATION_SUCCESS
        } else {
            Messages.REGISTRATION_FAIL
        }
    }
}
