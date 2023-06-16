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
    single { LabWorkService() }
    single { LabWorkRepository() }
    single { UserCollection() }
    single { CommandExecutor() }
    single { RequestHandler() }
}

fun main() {
    startKoin {
        modules(koinModule)
    }

    val serverManager = ServerManager(12345)

    runBlocking {
        serverManager.startServer()
    }
}

