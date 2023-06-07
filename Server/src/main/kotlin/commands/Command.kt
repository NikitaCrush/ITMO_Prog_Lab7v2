package commands

import commandArguments.CommandArgument
import commandArguments.CommandType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import utils.LabWorkCollection
import utils.UserCollection

abstract class Command: KoinComponent {
    val labWorkCollection: LabWorkCollection by inject()
    val userCollection: UserCollection by inject()

    abstract val commandType: CommandType
    abstract val commandArgs: List<CommandArgument>

    abstract fun execute(args: List<Any>, token: String? = null): String
}
