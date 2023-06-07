package utils

import data.User
import java.util.Scanner

class ProfileReader {
    private val hashUtil = HashUtil()
    private val scanner = Scanner(System.`in`)

    private fun readUsername(): String {
        print("Enter your username: ")
        return scanner.nextLine()
    }

    private fun readPassword(): String {
        print("Enter your password: ")
        return scanner.nextLine().toString().let { hashUtil.hashPassword(it) }
    }
    fun readUser(): User {
        val username = readUsername()
        val password = readPassword()
        return User(username,password)
    }
}
