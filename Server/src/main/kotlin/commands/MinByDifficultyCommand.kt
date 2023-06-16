package commands

import commandArguments.CommandArgument
import commandArguments.CommandType

/**
 * The MinByDifficultyCommand class finds and prints a random lab work with the minimal difficulty in the collection.
 *
 * @property labWorkService The lab work collection to be used for searching.
 */
class MinByDifficultyCommand : Command() {
    override val commandType = CommandType.NO_ARG
    override val commandArgs = emptyList<CommandArgument>()

    override fun execute(args: List<Any>, token: String?): String {
        return labWorkService.minByDifficulty().toString()
    }
}
