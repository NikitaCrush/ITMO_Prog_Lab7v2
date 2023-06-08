package utils

import org.jose4j.jws.AlgorithmIdentifiers
import org.jose4j.jws.JsonWebSignature
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.consumer.JwtConsumerBuilder
import org.jose4j.keys.HmacKey
import org.jose4j.lang.JoseException
import databaseManager.DatabaseManager

class UserCollection(private val dbManager: DatabaseManager) {

    private val secretKey = System.getenv("SECRET_KEY")


    fun register(username: String, hashedPassword: String): Boolean {
        val connection = dbManager.connection
        val preparedStatement = connection?.prepareStatement("INSERT INTO Users (username, password_hash) VALUES (?, ?)")

        preparedStatement?.setString(1, username)
        preparedStatement?.setString(2, hashedPassword)

        return try {
            preparedStatement?.executeUpdate()
            true
        } catch (ex: Exception) {
            println("Error during user registration: ${ex.message}")
            false
        }
    }

    fun login(username: String, hashedPassword: String): String? {
        val connection = dbManager.connection
        val preparedStatement = connection?.prepareStatement("SELECT * FROM Users WHERE username = ?")

        preparedStatement?.setString(1, username)

        val resultSet = preparedStatement?.executeQuery()

        if (resultSet?.next() == true) {
            val storedPasswordHash = resultSet.getString("password_hash")

            // compare hashed password directly with stored hash
            if (hashedPassword == storedPasswordHash) {
                val claims = JwtClaims()
                claims.setClaim("username", username)
                claims.setExpirationTimeMinutesInTheFuture(30f)

                val jws = JsonWebSignature()
                jws.payload = claims.toJson()
                jws.key = HmacKey(secretKey.toByteArray())
                jws.algorithmHeaderValue = AlgorithmIdentifiers.HMAC_SHA256

                val token = jws.compactSerialization

                return ("log in successful, your token is: $token")
            }
        }

        return null
    }

    fun validateToken(token: String): String? {
        val jwtConsumer = JwtConsumerBuilder()
            .setRequireExpirationTime()
            .setAllowedClockSkewInSeconds(30)
            .setVerificationKey(HmacKey(secretKey.toByteArray()))
            .build()

        return try {
            val claims = jwtConsumer.processToClaims(token)
            claims.getClaimValue("username", String::class.java)
        } catch (e: JoseException) {
            // Handle exception, possibly log it, and return null
            println("Error during token validation: ${e.message}")
            null
        }
    }

}
