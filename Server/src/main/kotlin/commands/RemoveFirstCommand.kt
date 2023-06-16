package commands

import commandArguments.CommandArgument
import commandArguments.CommandType

/**
 * The RemoveFirstCommand class removes the first element in the lab work collection.
 *
 * @property labWorkService The lab work collection to remove the first element from.
 */
class RemoveFirstCommand : Command() {
    override val commandType = CommandType.NO_ARG
    override val commandArgs = emptyList<CommandArgument>()

    override fun execute(args: List<Any>, token: String?): String {
        if (token == null) {
            throw IllegalArgumentException("No token provided.")
        }

        val owner = userCollection.validateToken(token)
            ?: throw IllegalArgumentException("Invalid token.")

        labWorkService.removeFirst(owner)
        return "First element removed successfully."
    }

}
