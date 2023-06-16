package commandsForRegAndLogIn

import commandArguments.CommandArgument
import commandArguments.CommandType
import commands.Command
import data.Messages
import data.User
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * The LoginCommand class handles the process of user login.
 *
 * @property userCollection The collection of users where the user's credentials will be verified.
 */
class LoginCommand : Command() {
    override val commandType = CommandType.USER_LOGIN
    override val commandArgs = listOf(CommandArgument("login", "User"))


    /**
     * Attempts to log in a user with the provided credentials.
     *
     * @param args A list of arguments where the first element should be the JSON representation of a User.
     * @param token An optional token for current logged in user.
     * @return A String message indicating the result of the login attempt.
     */
    override fun execute(args: List<Any>, token: String?): String {
        if (args.size != 1) {
            throw IllegalArgumentException("Login command expects 1 argument, but got ${args.size}.")
        }

        val user = args[0] as? String
            ?: throw IllegalArgumentException("Argument for login command is not of type String.")

        val deserializedUser: User = Json.decodeFromString(user)

        val loginResult = userCollection.login(deserializedUser.username, deserializedUser.password)
        return loginResult ?: Messages.LOGIN_FAIL
    }
}

