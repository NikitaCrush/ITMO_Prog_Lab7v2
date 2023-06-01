package server

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import utils.CommandExecutor
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import kotlinx.coroutines.*
import java.util.concurrent.Executors
import commandArguments.*

class ServerManager(private val port: Int) {
    private var serverSocket: ServerSocket? = null
    private var commandExecutor: CommandExecutor? = null
    private val requestExecutor = Executors.newCachedThreadPool()

    fun startServer(commandExecutor: CommandExecutor) = runBlocking {
        this@ServerManager.commandExecutor = commandExecutor
        serverSocket = ServerSocket(port)
        println("Server started on port $port")

        while (true) {
            val clientSocket = serverSocket!!.accept()
            println("Client connected: ${clientSocket.inetAddress.hostAddress}")

            // create a new task for each client connection
            requestExecutor.execute {
                handleCommands(clientSocket)
            }
        }
    }

//    fun stopServer() {
//        serverSocket?.close()
//        requestExecutor.shutdown()
//        responseExecutor.shutdown()
//    }

    private fun handleCommands(clientSocket: Socket) {
        val reader = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
        val writer = PrintWriter(BufferedWriter(OutputStreamWriter(clientSocket.getOutputStream())))

        // Send the list of commands to the client
        sendAvailableCommands(writer)

        // Process commands from the client
        while (true) {
            try {
                processClientCommand(reader, writer)
            } catch (e: Exception) {
                // Send error response back to client
                sendResponse(Response(false, "Error processing command: ${e.message}"), writer)
            }
        }
    }


    private fun sendAvailableCommands(writer: PrintWriter) {
        val commandMap = commandExecutor?.getAvailableCommands()?.map { it.key to it.value.commandType }?.toMap()
        writer.println(Json.encodeToString(commandMap))
        writer.flush()
    }




    private fun processClientCommand(reader: BufferedReader, writer: PrintWriter) {
        val commandData = receiveCommandData(reader)
        val response = executeCommand(commandData)
        sendResponse(response, writer)
    }

    private fun receiveCommandData(reader: BufferedReader): CommandData {
        val serializedCommand = reader.readLine()
        return Json.decodeFromString(serializedCommand ?: "")
    }

    private fun executeCommand(commandData: CommandData): Response {
        val command = commandExecutor?.getCommand(commandData.commandName) ?: return Response(false, "Command not found: ${commandData.commandName}")

        return try {
            val responseMessage = command.execute(commandData.arguments.map { it.value!! })  // Assuming the execute() method returns a response message.
            Response(true, responseMessage)
        } catch (e: Exception) {
            Response(false, "Error executing command: ${e.message}")
        }
    }

    private fun sendResponse(response: Response, writer: PrintWriter) {
        val serializedResponse = Json.encodeToString(response)
        writer.println(serializedResponse)
        writer.flush()
    }

    private fun closeClientConnection(clientSocket: Socket) {
        println("Closing client connection...")
        clientSocket.close()
    }
}
