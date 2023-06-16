package commands

import commandArguments.CommandArgument
import commandArguments.CommandType
import data.Messages

/**
 * The ClearCommand class clears the lab work collection.
 *
 * @property labWorkService The lab work collection to be cleared.
 */
class ClearCommand : Command() {
    override val commandType = CommandType.NO_ARG
    override val commandArgs = emptyList<CommandArgument>()

    override fun execute(args: List<Any>, token: String?): String {
        if (token == null) {
            throw IllegalArgumentException("No token provided.")
        }

        val owner = userCollection.validateToken(token)
            ?: throw IllegalArgumentException("Invalid token.")

        labWorkService.clear(owner)
        return Messages.LAB_WORK_SUCCESS_CLEAR
    }

}
