package commands

import commandArguments.CommandArgument
import commandArguments.CommandType
import data.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * The AddCommand class adds a new lab work to the collection.
 *
 * @property labWorkCollection The lab work collection to add the lab work to.
 */
class AddCommand : Command() {

    override val commandType = CommandType.LABWORK_ARG
    override val commandArgs = listOf(CommandArgument("labWork", "LabWork"))

    override fun execute(args: List<Any>): String {
        if (args.isEmpty()) {
            throw IllegalArgumentException("Add command expects 1 argument, but got none.")
        }

        val labWorkJson = args[0] as? String
            ?: throw IllegalArgumentException("Argument for add command is not of type String.")

        val labWork = Json.decodeFromString<LabWork>(labWorkJson)

        labWorkCollection.add(labWork)
        return Messages.LAB_WORK_SUCCESS_ADD
    }

}
