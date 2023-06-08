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
                if (loggedInUser == null) {
                    throw IllegalStateException("User must be logged in to perform this action.")
                }
                emptyList()}
            CommandType.SINGLE_ARG -> {
                if (loggedInUser == null) {
                    throw IllegalStateException("User must be logged in to perform this action.")
                }
                if (parameters.isEmpty()) {
                    throw IllegalArgumentException("A parameter is required for this command.")
                }
                listOf(CommandArgument("arg", "String", parameters[0]))
            }
            CommandType.LABWORK_ARG -> {
                if (loggedInUser == null) {
                    throw IllegalStateException("User must be logged in to perform this action.")
                }
                val serializedLabWork = getSerializedLabWork()
                listOf(CommandArgument("labWork", "LabWork", serializedLabWork))
            }
            CommandType.ARG_AND_LABWORK -> {
                if (loggedInUser == null) {
                    throw IllegalStateException("User must be logged in to perform this action.")
                }
                if (parameters.isEmpty()) {
                    throw IllegalArgumentException("A parameter is required for this command.")
                }
                val serializedLabWork = getSerializedLabWork()
                listOf(
                    CommandArgument("arg", "String", parameters[0]),
                    CommandArgument("labWork", "LabWork", serializedLabWork)
                )
            }
            CommandType.USER_REGISTRATION -> {
                if (loggedInUser != null) {
                    throw IllegalStateException("Already logged in. Please log out before registering a new user.")
                }
                val serializedUser = getSerializedUser()

                listOf(CommandArgument("reg", "User", serializedUser))
            }

            CommandType.USER_LOGIN -> {
                if (loggedInUser != null) {
                    throw IllegalStateException("Already logged in. Please log out before logging in again.")
                }
                val serializedUser = getSerializedUser()

                listOf(CommandArgument("login", "User", serializedUser))

            }

            else -> throw IllegalArgumentException("Command type not supported.")
        }

        return Pair(CommandData(commandName, arguments, clientManager.token), arguments)
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

}


//
//private fun handleCommands(clientSocket: Socket) {
//    val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
//    val writer = PrintWriter(BufferedWriter(OutputStreamWriter(clientSocket.getOutputStream())))
//
//    // Send the list of commands to the client
//    sendAvailableCommands(writer)
//
//    // Process commands from the client
//    while (true) {
//        try {
//            processClientCommand(reader, writer)
//        } catch (e: Exception) {
//            // Send error response back to client
//            sendResponse(Response(false, "Error processing command: ${e.message}"), writer)
//        }
//    }
//}