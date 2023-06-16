package server

import commandArguments.CommandData
import commandArguments.Response
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import utils.CommandExecutor
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.Socket
import java.util.concurrent.Executors

/**
 * Class responsible for handling incoming client requests and sending responses.
 * Each client is processed in a new thread.
 */
class RequestHandler : KoinComponent {

    private val responseExecutor = Executors.newFixedThreadPool(10)
    private val commandExecutor: CommandExecutor by inject()

    /**
     * Main method to handle client connection.
     * Reads and writes data using the provided client's socket.
     *
     * @param clientSocket the socket connection to the client.
     */
    fun handle(clientSocket: Socket) {
        handleSocketConnection(clientSocket) { reader, writer ->
            sendAvailableCommands(writer)
            while (true) {
                try {
                    processClientCommand(reader, writer)
                } catch (e: Exception) {
                    sendResponse(Response(false, "Error processing command: ${e.message}"), writer)
                }
            }
        }
    }

    /**
     * Manages the client socket connection for reading and writing.
     *
     * @param socket client's socket.
     * @param handler function for handling buffered reader and print writer.
     */
    private fun handleSocketConnection(socket: Socket, handler: (BufferedReader, PrintWriter) -> Unit) {
        BufferedReader(InputStreamReader(socket.getInputStream())).use { reader ->
            PrintWriter(BufferedWriter(OutputStreamWriter(socket.getOutputStream()))).use { writer ->
                handler(reader, writer)
            }
        }
    }

    /**
     * Sends the available commands to the client.
     *
     * @param writer PrintWriter for the client socket's output stream.
     */
    private fun sendAvailableCommands(writer: PrintWriter) {
        val commandMap = commandExecutor.getAvailableCommands().map { it.key to it.value.commandType }.toMap()
        writer.println(Json.encodeToString(commandMap))
        writer.flush()
    }

    /**
     * Processes the received client command.
     *
     * @param reader BufferedReader for the client socket's input stream.
     * @param writer PrintWriter for the client socket's output stream.
     */
    private fun processClientCommand(reader: BufferedReader, writer: PrintWriter) {
        val commandData = receiveCommandData(reader)
        val response = executeCommand(commandData)
        sendResponse(response, writer)
    }

    /**
     * Receives and deserializes the command data from the client.
     *
     * @param reader BufferedReader for the client socket's input stream.
     * @return deserialized CommandData.
     */
    private fun receiveCommandData(reader: BufferedReader): CommandData {
        val serializedCommand = reader.readLine()
        return Json.decodeFromString(serializedCommand ?: "")
    }

    /**
     * Executes the command received from the client.
     *
     * @param commandData the command data received from the client.
     * @return response to the executed command.
     */
    private fun executeCommand(commandData: CommandData): Response {
        val command = commandExecutor.getCommand(commandData.commandName) ?: return Response(
            false,
            "Command not found: ${commandData.commandName}"
        )
        val responseMessage = command.execute(commandData.arguments.map { it.value!! }, commandData.token)
        return Response(true, responseMessage)
    }

    /**
     * Sends response to the client.
     *
     * @param response response to be sent to the client.
     * @param writer PrintWriter for the client socket's output stream.
     */
    private fun sendResponse(response: Response, writer: PrintWriter) {
        responseExecutor.execute {
            val serializedResponse = Json.encodeToString(response)
            writer.println(serializedResponse)
            writer.flush()
        }
    }
}
