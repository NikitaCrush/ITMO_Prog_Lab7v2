package commands

import commandArguments.CommandArgument
import commandArguments.CommandType
import commands.Command
import data.Messages

class LogoutCommand : Command() {
    override val commandType = CommandType.USER_LOGOUT
    override val commandArgs = emptyList<CommandArgument>()

    override fun execute(args: List<Any>, token: String?): String {
        return Messages.LOGOUT_SUCCESS
    }
}
