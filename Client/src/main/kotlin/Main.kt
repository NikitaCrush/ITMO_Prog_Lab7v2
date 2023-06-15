import utils.LabWorkReader
import utils.LabWorkValidator
import client.*
import data.Messages
import utils.ProfileReader

fun main() {
    // Initialize required instances
    val labWorkValidator = LabWorkValidator()
    val labWorkReader = LabWorkReader({ readlnOrNull() ?: throw IllegalStateException("No input provided") }, labWorkValidator)
    val profileReader = ProfileReader()
    // Initialize the clientManager
    val clientManager = ClientManager("localhost", 12345)

    // Initialize the command interpreter
    val commandInterpreter = CommandInterpreter(labWorkReader, clientManager, profileReader)
    clientManager.setCommandInterpreter(commandInterpreter)
    clientManager.connect()

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

            if (commandData.commandName != "register" && commandData.commandName != "login" && clientManager.token == null) {
                println("Please login before executing commands.")
                continue
            }

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
        } catch (e: IllegalStateException) {
            println(e.message)
        }
    }
    clientManager.disconnect()
}
