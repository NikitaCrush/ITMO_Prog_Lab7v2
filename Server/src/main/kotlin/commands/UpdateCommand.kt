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
 * @property labWorkService The lab work collection to be updated.
 */


class UpdateCommand : Command() {
    override val commandType = CommandType.ARG_AND_LABWORK
    override val commandArgs = listOf(CommandArgument("id", "String"), CommandArgument("labwork", "LabWork"))

    override fun execute(args: List<Any>, token: String?): String {
        if (token == null || args.size < 2 || args[0] !is String) {
            return "ID, LabWork object and/or token is not provided or has an incorrect format."
        }

        val id: Long = try {
            args[0].toString().toLong()
        } catch (e: NumberFormatException) {
            return "Invalid ID format. Please enter a valid number."
        }

        val labWorkJson = args[1] as String
        val updatedLabWork = Json.decodeFromString<LabWork>(labWorkJson)

        val owner = userCollection.validateToken(token)
            ?: throw IllegalArgumentException("Invalid token.")

        val labWorkToUpdate = labWorkService.show().find { it.id == id && it.owner == owner }

        return if (labWorkToUpdate != null) {
            labWorkService.update(id, updatedLabWork)
            "Lab work with ID: $id has been updated."
        } else {
            "No lab work found with ID: $id that belongs to the current user."
        }
    }
}