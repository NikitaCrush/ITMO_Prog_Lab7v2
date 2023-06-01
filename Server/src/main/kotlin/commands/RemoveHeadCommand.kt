package commands

import commandArguments.CommandArgument
import commandArguments.CommandType

/**
 * The RemoveHeadCommand class removes and returns the first element in the lab work collection.
 *
 * @property labWorkCollection The lab work collection to remove the first element from.
 */
class RemoveHeadCommand : Command() {
    override val commandType = CommandType.NO_ARG
    override val commandArgs = emptyList<CommandArgument>()

    override fun execute(args: List<Any>): String {
        return labWorkCollection.removeHead().toString()
    }
}
