package data

import kotlinx.serialization.Serializable

/**
 * Data class for a user.
 *
 * @property username The username of the user.
 * @property password The password of the user.
 */
@Serializable
data class User(
    val username: String,
    val password: String
)
