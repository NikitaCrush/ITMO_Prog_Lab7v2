package commandsForRegAndLogIn

import commandArguments.CommandArgument
import commandArguments.CommandType
import commands.Command
import data.Messages

/**
 * The LogoutCommand class handles the process of user logout.
 *
 * @property userCollection The collection of users where the current logged-in user will be logged out.
 */
class LogoutCommand : Command() {
    override val commandType = CommandType.USER_LOGOUT
    override val commandArgs = emptyList<CommandArgument>()

    /**
     * Logs out the currently logged-in user.
     *
     * @param args A list of arguments, which should be empty for this command.
     * @param token An optional token for current logged in user.
     * @return A String message indicating the result of the logout process.
     */
    override fun execute(args: List<Any>, token: String?): String {
        return Messages.LOGOUT_SUCCESS
    }
}
