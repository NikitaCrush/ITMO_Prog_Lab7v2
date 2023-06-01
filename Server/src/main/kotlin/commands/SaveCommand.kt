package commands

import commandArguments.CommandArgument
import commandArguments.CommandType
import data.Messages

/**
 * The SaveCommand class saves the lab work collection to a file.
 *
 * @property labWorkCollection The lab work collection to be saved.
 */
class SaveCommand : Command() {
    override val commandType = CommandType.NO_ARG
    override val commandArgs = emptyList<CommandArgument>()

    override fun execute(args: List<Any>): String {
        labWorkCollection.saveToFile()
        return Messages.LAB_WORK_SUCCESS_SAVE
    }
}
