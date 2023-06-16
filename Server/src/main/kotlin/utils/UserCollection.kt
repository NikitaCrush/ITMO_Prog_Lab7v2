package utils

import org.jose4j.jws.AlgorithmIdentifiers
import org.jose4j.jws.JsonWebSignature
import org.jose4j.jwt.JwtClaims
import org.jose4j.jwt.consumer.JwtConsumerBuilder
import org.jose4j.keys.HmacKey
import org.jose4j.lang.JoseException
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import databaseManager.DatabaseManager
import java.sql.ResultSet

/**
 * This class manages operations related to the user, including registration, login, and token validation.
 * Utilizes dependency injection (via Koin) for managing [DatabaseManager].
 */
class UserCollection : KoinComponent {

    /**
     * Injected instance of [DatabaseManager] for managing database connections and operations.
     */
    private val dbManager: DatabaseManager by inject()

    /**
     * The secret key used for generating and validating JWT tokens.
     */
    private val secretKey = System.getenv("SECRET_KEY")

    /**
     * Helper function to execute a SQL query with parameters.
     *
     * @param query The SQL query string to be executed.
     * @param parameters The parameters to be included in the SQL query.
     * @return [ResultSet] object containing the results of the executed query.
     */
    private fun executeQuery(query: String, vararg parameters: Any?): ResultSet? {
        val connection = dbManager.connection
        val preparedStatement = connection?.prepareStatement(query)
        parameters.forEachIndexed { index, param -> preparedStatement?.setObject(index + 1, param) }
        return preparedStatement?.executeQuery()
    }

    /**
     * Helper function to execute a SQL update with parameters.
     *
     * @param query The SQL update string to be executed.
     * @param parameters The parameters to be included in the SQL query.
     * @return A boolean indicating if the update was successful.
     */
    private fun executeUpdate(query: String, vararg parameters: Any?): Boolean {
        val connection = dbManager.connection
        val preparedStatement = connection?.prepareStatement(query)
        parameters.forEachIndexed { index, param -> preparedStatement?.setObject(index + 1, param) }
        return try {
            preparedStatement?.executeUpdate() != null
        } catch (ex: Exception) {
            println("Error during SQL update execution: ${ex.message}")
            false
        }
    }

    /**
     * Register a new user with a given username and hashed password.
     *
     * @param username The username of the new user.
     * @param hashedPassword The hashed password of the new user.
     * @return A boolean indicating if the registration was successful.
     */
    fun register(username: String, hashedPassword: String): Boolean {
        val query = "INSERT INTO Users (username, password_hash) VALUES (?, ?)"
        return executeUpdate(query, username, hashedPassword)
    }

    /**
     * Logs in a user given a username and hashed password.
     * Generates and returns a JWT token for a successful login.
     *
     * @param username The username of the user.
     * @param hashedPassword The hashed password of the user.
     * @return A string containing the JWT token if login was successful, null otherwise.
     */
    fun login(username: String, hashedPassword: String): String? {
        val storedPasswordHash = getStoredPasswordHash(username)
        return if (hashedPassword == storedPasswordHash) generateToken(username) else null
    }

    /**
     * Retrieves the stored password hash for a given username.
     *
     * @param username The username for which to retrieve the password hash.
     * @return The password hash as a string if found, null otherwise.
     */
    private fun getStoredPasswordHash(username: String): String? {
        val query = "SELECT * FROM Users WHERE username = ?"
        val resultSet = executeQuery(query, username)
        return if (resultSet?.next() == true) resultSet.getString("password_hash") else null
    }

    /**
     * Generates a JWT token for a given username. The token is set to expire after 30 minutes.
     *
     * @param username The username for which to generate the token.
     * @return A string message containing the generated JWT token.
     */
    private fun generateToken(username: String): String {
        val claims = JwtClaims()
        claims.setClaim("username", username)
        claims.setExpirationTimeMinutesInTheFuture(30f)

        val jws = JsonWebSignature()
        jws.payload = claims.toJson()
        jws.key = HmacKey(secretKey.toByteArray())
        jws.algorithmHeaderValue = AlgorithmIdentifiers.HMAC_SHA256

        val token = jws.compactSerialization

        return "log in successful, your token is: $token"
    }

    /**
     * Validate a given JWT token.
     *
     * @param token The JWT token to be validated.
     * @return The username if the token is valid, null otherwise.
     */
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
            println("Error during token validation: ${e.message}")
            null
        }
    }
}
