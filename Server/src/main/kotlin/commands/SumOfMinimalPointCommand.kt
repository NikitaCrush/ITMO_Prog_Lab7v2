package commands

import commandArguments.CommandArgument
import commandArguments.CommandType

/**
 * The SumOfMinimalPointCommand class calculates the sum of the minimalPoint values of all
 * lab works in the collection.
 *
 * @property labWorkCollection The lab work collection to be used for calculations.
 */
class SumOfMinimalPointCommand : Command() {
    override val commandType = CommandType.NO_ARG
    override val commandArgs = emptyList<CommandArgument>()

    override fun execute(args: List<Any>): String {
        return labWorkCollection.sumOfMinimalPoint().toString()
    }
}
