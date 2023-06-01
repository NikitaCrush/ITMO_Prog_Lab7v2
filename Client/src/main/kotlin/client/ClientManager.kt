package client

import commandArguments.CommandData
import commandArguments.CommandType
import commandArguments.Response
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.*
import java.net.Socket
import java.security.MessageDigest

class ClientManager(private val host: String, private val port: Int) {
    private var socket: Socket? = null
    private var reader: BufferedReader? = null
    private var writer: PrintWriter? = null
    var commandList: Map<String, CommandType> = emptyMap()




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
        return Json.decodeFromString(serializedResponse)
    }
    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-512")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }


}
