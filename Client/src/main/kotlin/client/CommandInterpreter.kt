package client

import commandArguments.CommandArgument
import commandArguments.CommandData
import commandArguments.CommandType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import utils.LabWorkReader
import utils.ProfileReader

class CommandInterpreter(
    private val labWorkReader: LabWorkReader,
    private val clientManager: ClientManager,
    private val profileReader: ProfileReader
) {
    private var loggedInUser: String? = null

    fun interpret(input: String): Pair<CommandData, List<CommandArgument>> {
        val commandParts = input.split(" ")
        val commandName = commandParts[0]
        val parameters = commandParts.drop(1)
        val commandType = findCommandType(commandName) ?: throw IllegalArgumentException("Command not found.")

        val arguments = when (commandType) {
            CommandType.NO_ARG -> {
                requireLoggedIn()
                emptyList()
            }

            CommandType.SINGLE_ARG -> {
                requireLoggedIn()
                requireParameter(parameters)
                listOf(CommandArgument("arg", "String", parameters[0]))
            }

            CommandType.LABWORK_ARG -> {
                requireLoggedIn()
                val serializedLabWork = getSerializedLabWork()
                listOf(CommandArgument("labWork", "LabWork", serializedLabWork))
            }

            CommandType.ARG_AND_LABWORK -> {
                requireLoggedIn()
                requireParameter(parameters)
                val serializedLabWork = getSerializedLabWork()
                listOf(
                    CommandArgument("arg", "String", parameters[0]),
                    CommandArgument("labWork", "LabWork", serializedLabWork)
                )
            }

            CommandType.USER_REGISTRATION -> {
                requireLoggedOut()
                val serializedUser = getSerializedUser()
                listOf(CommandArgument("reg", "User", serializedUser))
            }

            CommandType.USER_LOGIN -> {
                requireLoggedOut()
                val serializedUser = getSerializedUser()
                listOf(CommandArgument("login", "User", serializedUser))
            }
            CommandType.USER_LOGOUT -> {
                requireLoggedIn()
                // Clear logged in user
                loggedInUser = null
                // Clear client token
                clientManager.token = null
                emptyList()
            }

            else -> throw IllegalArgumentException("Command type not supported.")
        }

        return Pair(CommandData(commandName, arguments, clientManager.token), arguments)
    }

    fun failedLoginOrRegistration() {
        loggedInUser = null
    }

    private fun findCommandType(commandName: String): CommandType? {
        return clientManager.commandList[commandName]
    }

    private fun getSerializedLabWork(): String {
        // Check if a user is logged in before attempting to read lab work
        val owner = loggedInUser ?: throw IllegalStateException("No user logged in.")
        val labWork = labWorkReader.readLabWork(owner)
        return Json.encodeToString(labWork)
    }

    private fun getSerializedUser(): String {
        val user = profileReader.readUser()
        loggedInUser = user.username  // Store the username after reading user data
        return Json.encodeToString(user)
    }

    private fun requireLoggedIn() {
        if (loggedInUser == null) {
            throw IllegalStateException("User must be logged in to perform this action.")
        }
    }

    private fun requireLoggedOut() {
        if (loggedInUser != null) {
            throw IllegalStateException("Already logged in. Please log out before logging in again.")
        }
    }

    private fun requireParameter(parameters: List<String>) {
        if (parameters.isEmpty()) {
            throw IllegalArgumentException("A parameter is required for this command.")
        }
    }
}