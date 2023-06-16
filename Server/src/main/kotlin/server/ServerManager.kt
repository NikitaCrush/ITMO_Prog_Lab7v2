package server

import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.ServerSocket
import java.util.concurrent.Executors

/**
 * This class is responsible for starting and managing the server.
 *
 * @property port The port on which the server will listen for incoming connections.
 */
class ServerManager(
    private val port: Int
) : KoinComponent {
    private val serverSocket: ServerSocket by lazy { ServerSocket(port) }
    private val requestExecutor = Executors.newCachedThreadPool()
    private val requestHandler: RequestHandler by inject()

    /**
     * Starts the server and begins accepting clients.
     */
    fun startServer() = runBlocking {
        println("Server started on port $port")
        while (true) {
            val clientSocket = serverSocket.accept()
            println("Client connected: ${clientSocket.inetAddress.hostAddress}")
            requestExecutor.execute {
                requestHandler.handle(clientSocket)
            }
        }
    }
}
