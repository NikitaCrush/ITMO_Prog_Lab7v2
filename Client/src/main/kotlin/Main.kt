import utils.LabWorkReader
import utils.Validator
import client.*
import data.Messages

fun main() {
    // Initialize required instances
    val validator = Validator()
    val labWorkReader = LabWorkReader({ readlnOrNull() ?: throw IllegalStateException("No input provided") }, validator)

    // Initialize the ClientManager
    val clientManager = ClientManager("localhost", 12345)
    clientManager.connect()

    // Initialize the command interpreter
    val commandInterpreter = CommandInterpreter(labWorkReader, clientManager)
    println(Messages.WELCOME)
    println(Messages.ENTER_HELP)

    while (true) {
        print("> ")
        val input = readlnOrNull() ?: continue
        if (input.lowercase() == "exit") {
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
            // This is where you handle the exception and display a friendly error message
            println(e.message)
        }
    }


    clientManager.disconnect()
}
