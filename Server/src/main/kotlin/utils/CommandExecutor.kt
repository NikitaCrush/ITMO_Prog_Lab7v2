package utils

import commandsForRegAndLogIn.*
import commands.*
import java.util.*

/**
 * Class responsible for managing and executing commands.
 *
 */
class CommandExecutor {
    private val commandMap: MutableMap<String, Command> by lazy {
        mutableMapOf<String, Command>().apply {
            this["help"] = HelpCommand(this@CommandExecutor)
            this["info"] = InfoCommand()
            this["show"] = ShowCommand()
            this["add"] = AddCommand()
            this["update"] = UpdateCommand()
            this["remove_by_id"] = RemoveByIdCommand()
            this["clear"] = ClearCommand()
            this["remove_first"] = RemoveFirstCommand()
            this["remove_head"] = RemoveHeadCommand()
            this["add_if_max"] = AddIfMaxCommand()
            this["sum_of_minimal_point"] = SumOfMinimalPointCommand()
            this["min_by_difficulty"] = MinByDifficultyCommand()
            this["print_unique_minimal_point"] = PrintUniqueMinimalPointCommand()
            this["register"] = RegisterCommand()
            this["login"] = LoginCommand()
            this["logout"] = LogoutCommand()
        }
    }


    /**
     * Retrieves a command instance by its name.
     *
     * @param name The name of the command to retrieve.
     * @return The command instance if found, null otherwise.
     */
    fun getCommand(name: String): Command? {
        return commandMap[name.lowercase(Locale.getDefault())]
    }

    /**
     * Retrieves the available commands.
     *
     * @return A map of command names to their corresponding [Command] instances.
     */
    fun getAvailableCommands(): Map<String, Command> {
        return commandMap
    }
}
