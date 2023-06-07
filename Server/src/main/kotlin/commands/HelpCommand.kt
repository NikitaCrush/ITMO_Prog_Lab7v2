package commands

import commandArguments.CommandArgument
import commandArguments.CommandType
import utils.CommandExecutor

/**
 * The HelpCommand class displays help information for all available commands.
 *
 * @property commandExecutor The command executor used to get the list of available commands.
 */
class HelpCommand(private val commandExecutor: CommandExecutor) : Command() {
    override val commandType = CommandType.NO_ARG
    override val commandArgs = emptyList<CommandArgument>()

    private val commandDescriptions = mapOf(
        "help" to "help : print help for available commands",
        "info" to "info : print information about the collection (type, initialization date, number of items, etc.) in the standard output.",
        "show" to "show : print all items of the collection in the string representation in the standard output",
        "add" to "add : add a new element to the collection",
        "update" to "update : update the value of an element in the collection whose id is equal to the given one",
        "remove_by_id" to "remove_by_id id : remove an item from the collection by its id",
        "clear" to "clear : clear the collection",
        "save" to "save : save the collection to a file",
        "execute_script" to "execute_script file_name : read and execute a script from the specified file. The script contains commands in the same form as the user enters them in interactive mode.",
        "exit" to "exit : end the program (without saving it to a file)",
        "remove_first" to "remove_first : remove the first element from the collection",
        "remove_head" to "remove_head : display the first element of the collection and remove it",
        "add_if_max" to "add_if_max {element} : add a new element to the collection if its value exceeds the value of the largest element in this collection",
        "sum_of_minimal_point" to "sum_of_minimal_point : print the sum of minimalPoint values for all items in the collection",
        "min_by_difficulty" to "min_by_difficulty : print any object in the collection whose value is minimal",
        "print_unique_minimal_point" to "print_unique_minimal_point : print the unique values of the minimalPoint field of all elements in the collection",
    )

    override fun execute(args: List<Any>, token: String?): String {
        val availableCommands = commandExecutor.getAvailableCommands()
        val helpText = StringBuilder()

        availableCommands.forEach { (commandName, _) ->
            helpText.append(commandDescriptions[commandName] ?: "Unknown command: $commandName")
            helpText.append("\n")
        }

        return helpText.toString()
    }
}
