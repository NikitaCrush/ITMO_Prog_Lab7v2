package client

import commandArguments.CommandArgument
import commandArguments.CommandData
import commandArguments.CommandType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import utils.LabWorkReader

class CommandInterpreter(private val labWorkReader: LabWorkReader, private val clientManager: ClientManager) {
    fun interpret(input: String): Pair<CommandData, List<CommandArgument>> {
        val commandParts = input.split(" ")
        val commandName = commandParts[0]
        val parameters = commandParts.drop(1)
        val commandType = findCommandType(commandName) ?: throw IllegalArgumentException("Command not found.")


        val arguments = when (commandType) {
            CommandType.NO_ARG -> emptyList()
            CommandType.SINGLE_ARG -> {
                if (parameters.isEmpty()) {
                    throw IllegalArgumentException("A parameter is required for this command.")
                }
                listOf(CommandArgument("arg", "String", parameters[0]))
            }
            CommandType.LABWORK_ARG -> {
                val serializedLabWork = getSerializedLabWork()
                listOf(CommandArgument("labWork", "LabWork", serializedLabWork))
            }
            CommandType.ARG_AND_LABWORK -> {
                if (parameters.isEmpty()) {
                    throw IllegalArgumentException("A parameter is required for this command.")
                }
                val serializedLabWork = getSerializedLabWork()
                listOf(
                    CommandArgument("arg", "String", parameters[0]),
                    CommandArgument("labWork", "LabWork", serializedLabWork)
                )
            }
            else -> throw IllegalArgumentException("Command type not supported.")
        }

        return Pair(CommandData(commandName,  arguments), arguments)
    }

    private fun findCommandType(commandName: String): CommandType? {
        return clientManager.commandList[commandName]
    }




    private fun getSerializedLabWork(): String {
        val labWork = labWorkReader.readLabWork()
        return Json.encodeToString(labWork)
    }
}
