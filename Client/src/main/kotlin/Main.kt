import utils.LabWorkReader
import utils.Validator
import client.*
import data.Messages
import utils.ProfileReader

fun main() {
    // Initialize required instances
    val validator = Validator()
    val labWorkReader = LabWorkReader({ readlnOrNull() ?: throw IllegalStateException("No input provided") }, validator)
    val profileReader = ProfileReader()

    // Initialize the ClientManager
    val clientManager = ClientManager("localhost", 12345)
    clientManager.connect()

    // Initialize the command interpreter
    val commandInterpreter = CommandInterpreter(labWorkReader, clientManager, profileReader)
    println(Messages.WELCOME)
    println(Messages.ENTER_HELP)

    while (true) {
        print("> ")
        val input = readlnOrNull() ?: continue
        if (input.lowercase() == "exit") {
            clientManager.disconnect()
            break
        }

        try {
            val (commandData, _) = commandInterpreter.interpret(input)
            val task = Task(commandData)

            commandData.arguments.forEach { argument ->
                if (argument.value.isNullOrEmpty()) {
                    print("Enter ${argument.name} (${argument.type}): ")
                    argument.value = readlnOrNull()
                }
            }

            val response = task.execute(clientManager)
            println(response.message)
        } catch (e: IllegalArgumentException) {
            println(e.message)
        }
    }
    clientManager.disconnect()
}