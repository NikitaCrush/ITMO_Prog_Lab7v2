package client

import commandArguments.CommandData
import commandArguments.CommandType
import commandArguments.Response
import data.Messages
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.*
import java.net.Socket

class ClientManager(private val host: String, private val port: Int) {
    private var socket: Socket? = null
    private var reader: BufferedReader? = null
    private var writer: PrintWriter? = null
    var commandList: Map<String, CommandType> = emptyMap()
    var token: String? = null
    private var commandInterpreter: CommandInterpreter? = null


    fun connect() {
        while (true) {
            try {
                socket = Socket(host, port)
                reader = BufferedReader(InputStreamReader(socket!!.getInputStream()))
                writer = PrintWriter(BufferedWriter(OutputStreamWriter(socket!!.getOutputStream())))

                val commandListJson: String = reader?.readLine() ?: "{}"
                commandList = Json.decodeFromString(commandListJson)

                break
            } catch (e: IOException) {
                println("Unable to connect to server. Retrying in 10 seconds...")
                Thread.sleep(10000)
            }
        }
    }

    fun disconnect() {
        reader?.close()
        writer?.close()
        socket?.close()
    }

    fun sendCommand(commandData: CommandData) {
        val commandWithArgs = commandData.copy(arguments = commandData.arguments.map { it.copy(value = it.value) })
        val serializedCommand = Json.encodeToString(commandWithArgs)
        writer?.println(serializedCommand)
        writer?.flush()
    }

    fun receiveResponse(): Response {
        val serializedResponse = reader?.readLine()
        if (serializedResponse.isNullOrBlank()) {
            throw IllegalStateException("No response received from the server.")
        }
        val response = Json.decodeFromString<Response>(serializedResponse)

        // Handle the token for a successful login.
        if (response.message.startsWith("log in successful, your token is: ")) {
            val token = response.message.substringAfter("log in successful, your token is: ")
            // Notify the CommandInterpreter about the received token.
            receiveToken(token)
            // Remove the token from the message before returning the response.
        } else if (response.message == Messages.LOGIN_FAIL || response.message == Messages.REGISTRATION_FAIL) {
            // Notify the CommandInterpreter about the failed login or registration.
            failedLoginOrRegistration()
        }

        return response
    }
    fun receiveToken(token: String) {
        this.token = token
    }
    fun setCommandInterpreter(commandInterpreter: CommandInterpreter) {
        this.commandInterpreter = commandInterpreter
    }

    fun failedLoginOrRegistration() {
        commandInterpreter?.failedLoginOrRegistration()
        this.token = null
    }
}
