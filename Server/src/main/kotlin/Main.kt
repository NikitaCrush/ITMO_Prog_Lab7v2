package server

import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.dsl.module
import utils.*
import java.util.*

val koinModule = module {
    single { Stack<String>() }
    single {
        val fileName = System.getenv("LAB_WORK_FILE") ?: "collection.json"
        LabWorkCollection(fileName)
    }
}

fun main() {
    startKoin {
        modules(koinModule)
    }

    val commandExecutor = CommandExecutor()
    val serverManager = ServerManager(12345) // assuming 12345 is your port number

    runBlocking {
        serverManager.startServer(commandExecutor)
    }
}
