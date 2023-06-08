package commands

import commandArguments.CommandArgument
import commandArguments.CommandType

/**
 * The RemoveByIdCommand class removes a lab work from the collection by providing a valid ID.
 *
 * @property labWorkCollection The lab work collection to remove the lab work from.
 */
class RemoveByIdCommand : Command() {
    override val commandType = CommandType.SINGLE_ARG
    override val commandArgs = listOf(CommandArgument("id", "String"))

    override fun execute(args: List<Any>, token: String?): String {
        if (token == null || args.size < 1 || args[0] !is String) {
            return "ID and/or token is not provided or has an incorrect format."
        }

        val id: Long = try {
            args[0].toString().toLong()
        } catch (e: NumberFormatException) {
            return "Invalid ID format. Please enter a valid number."
        }
        val owner = userCollection.validateToken(token)
            ?: throw IllegalArgumentException("Invalid token.")

        val labWork = labWorkCollection.show().find { it.id == id && it.owner == owner }

        if (labWork != null) {
            labWorkCollection.removeById(id)
            return "Lab work removed successfully."
        } else {
            return "No lab work found with the provided id that belongs to the current user."
        }
    }
}