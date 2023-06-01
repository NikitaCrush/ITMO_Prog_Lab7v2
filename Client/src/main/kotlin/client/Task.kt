package client

import commandArguments.CommandData
import commandArguments.Response


class Task(private val commandData: CommandData) {
    fun execute(clientManager: ClientManager): Response {
        clientManager.sendCommand(commandData)
        return clientManager.receiveResponse()
    }
}

