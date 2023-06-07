package commands

import commandArguments.CommandArgument
import commandArguments.CommandType
import data.LabWork
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

/**
 * The UpdateCommand class is responsible for updating a specific lab work in the collection
 * by providing a valid ID.
 *
 * @property labWorkCollection The lab work collection to be updated.
 */


class UpdateCommand : Command() {
    override val commandType = CommandType.ARG_AND_LABWORK
    override val commandArgs = listOf(CommandArgument("id", "String"), CommandArgument("labwork", "LabWork"), CommandArgument("token", "String"))

    override fun execute(args: List<Any>, token: String?): String {
        if (args.size < 3 || args[0] !is String) {
            return "ID, LabWork object and/or token is not provided or has an incorrect format."
        }

        val id: Long = try {
            args[0].toString().toLong()
        } catch (e: NumberFormatException) {
            return "Invalid ID format. Please enter a valid number."
        }

        val labWorkJson = args[1] as String
        val updatedLabWork = Json.decodeFromString<LabWork>(labWorkJson)



        val labWorkToUpdate = labWorkCollection.show().find { it.id == id }

        return if (labWorkToUpdate != null) {
            labWorkCollection.update(id, updatedLabWork)
            "Lab work with ID: $id has been updated."
        } else {
            "No lab work found with ID: $id."
        }
    }
}