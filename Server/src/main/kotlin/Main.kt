package server

import databaseManager.DatabaseManager
import kotlinx.coroutines.runBlocking
import org.koin.core.context.startKoin
import org.koin.dsl.module
import utils.*
import java.util.*

val koinModule = module {
    single { Stack<String>() }
    single { DatabaseManager() }
    single { LabWorkCollection()}
    single {UserCollection(DatabaseManager())}

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
