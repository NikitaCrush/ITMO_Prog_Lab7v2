package commands

import commandArguments.CommandArgument
import commandArguments.CommandType

/**
 * The InfoCommand class displays information about the lab work collection.
 *
 * @property labWorkCollection The lab work collection to get information about.
 */
class InfoCommand : Command() {
    override val commandType = CommandType.NO_ARG
    override val commandArgs = emptyList<CommandArgument>()

    override fun execute(args: List<Any>): String {
        return labWorkCollection.getInfo()
    }
}
