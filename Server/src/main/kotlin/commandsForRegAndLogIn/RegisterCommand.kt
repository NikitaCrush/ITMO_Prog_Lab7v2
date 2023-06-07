package commandsForRegAndLogIn

import commandArguments.CommandArgument
import commandArguments.CommandType
import commands.Command
import data.Messages
import data.User
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class RegisterCommand : Command() {
    override val commandType = CommandType.USER_REGISTRATION
    override val commandArgs = listOf(CommandArgument("reg", "User"))

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
