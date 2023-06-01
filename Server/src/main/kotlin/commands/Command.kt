package commands

import commandArguments.CommandArgument
import commandArguments.CommandType
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import utils.LabWorkCollection

abstract class Command: KoinComponent {
    val labWorkCollection: LabWorkCollection by inject()
    abstract val commandType: CommandType
    abstract val commandArgs: List<CommandArgument>

    abstract fun execute(args: List<Any>): String
}
