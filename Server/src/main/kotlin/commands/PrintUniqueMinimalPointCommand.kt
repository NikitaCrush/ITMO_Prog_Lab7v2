package commands

import commandArguments.CommandArgument
import commandArguments.CommandType

/**
 * The PrintUniqueMinimalPointCommand class prints the unique minimalPoint values of all
 * lab works in the collection.
 *
 * @property labWorkService The lab work collection to be used for printing unique minimalPoint values.
 */
class PrintUniqueMinimalPointCommand : Command() {
    override val commandType = CommandType.NO_ARG
    override val commandArgs = emptyList<CommandArgument>()

    override fun execute(args: List<Any>, token: String?): String {
        return labWorkService.printUniqueMinimalPoint().joinToString("\n")
    }
}
