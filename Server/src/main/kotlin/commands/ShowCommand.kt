package commands

import commandArguments.CommandArgument
import commandArguments.CommandType

/**
 * The ShowCommand class displays all lab works in the collection.
 *
 * @property labWorkService The lab work collection to be displayed.
 */
class ShowCommand : Command() {
    override val commandType = CommandType.NO_ARG
    override val commandArgs = emptyList<CommandArgument>()

    override fun execute(args: List<Any>, token: String?): String {
        return labWorkService.show().joinToString(separator = "\n")
    }
}
