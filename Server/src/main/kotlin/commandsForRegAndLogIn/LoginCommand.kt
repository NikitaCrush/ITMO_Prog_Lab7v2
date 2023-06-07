package commandsForRegAndLogIn

import commandArguments.CommandArgument
import commandArguments.CommandType
import commands.Command
import data.Messages
import data.User
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class LoginCommand : Command() {
    override val commandType = CommandType.USER_LOGIN
    override val commandArgs = listOf(CommandArgument("login", "User"))

    override fun execute(args: List<Any>, token: String?): String {
        if (args.size != 1) {
            throw IllegalArgumentException("Login command expects 1 argument, but got ${args.size}.")
        }

        val user = args[0] as? String
            ?: throw IllegalArgumentException("Argument for login command is not of type String.")

        val deserializedUser: User = Json.decodeFromString(user)

        return userCollection.login(deserializedUser.username, deserializedUser.password) ?: Messages.LOGIN_FAIL
    }
}

