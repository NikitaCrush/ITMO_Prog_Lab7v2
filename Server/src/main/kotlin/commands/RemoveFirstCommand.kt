package commands

import commandArguments.CommandArgument
import commandArguments.CommandType

/**
 * The RemoveFirstCommand class removes the first element in the lab work collection.
 *
 * @property labWorkCollection The lab work collection to remove the first element from.
 */
class RemoveFirstCommand : Command() {
    override val commandType = CommandType.NO_ARG
    override val commandArgs = emptyList<CommandArgument>()

    override fun execute(args: List<Any>): String {
        labWorkCollection.removeFirst()
        return "First element removed successfully."
    }
}
