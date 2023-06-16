package utils

import java.security.MessageDigest

/**
 * Provides utility methods for hashing.
 */
class HashUtil {

    /**
     * Hashes a password using SHA-512.
     *
     * @param password The password to be hashed.
     * @return The hashed password.
     */
     fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-512")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}