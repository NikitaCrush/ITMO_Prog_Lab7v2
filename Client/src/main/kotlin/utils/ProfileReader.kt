package utils

import data.User

/**
 * Reads and constructs User data.
 */
class ProfileReader {
    private val hashUtil = HashUtil()

    /**
     * Reads a username from the console.
     *
     * @return The username as a string.
     */
    private fun readUsername(): String {
        print("Enter your username: ")
        return readlnOrNull() ?: ""
    }

    /**
     * Reads a password from the console and hashes it.
     *
     * @return The hashed password as a string.
     */
    private fun readPassword(): String {
        print("Enter your password: ")
        val password = readlnOrNull() ?: ""
        return hashUtil.hashPassword(password)
    }

    /**
     * Reads a username and password from the console and constructs a User object.
     *
     * @return The constructed User object.
     */
    fun readUser(): User {
        val username = readUsername()
        val password = readPassword()
        val user = User(username,password)
        ProfileValidator().validate(user)  // Validate the user
        return user
    }
}
