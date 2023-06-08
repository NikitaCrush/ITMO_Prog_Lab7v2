package databaseManager

import java.io.File
import java.sql.Connection
import java.sql.DriverManager

class DatabaseManager {
    var connection: Connection? = null
        private set
    init {
        val lines = File("db_credentials.txt").readLines()
        val url = lines[0]
        val user = lines[1]
        val password = lines[2]

//        try {
            connection = DriverManager.getConnection(url, user, password)
            println("Connected to the PostgresSQL server successfully.")
//        } catch (e: SQLException) {
//            println(e.message)
//        }
    }

//    fun close() {
//        connection?.close()
//    }
}
