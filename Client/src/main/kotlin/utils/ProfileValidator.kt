package utils

import data.User

/**
 * Validates User data.
 */
class ProfileValidator {

    /**
     * Validates the given username and password.
     *
     * @param user The user whose data is to be validated.
     * @throws IllegalArgumentException If the username or password is empty.
     */
    fun validate(user: User) {
        if (user.username.isBlank()) {
            throw IllegalArgumentException("Username cannot be empty.")
        }
        if (user.password.isBlank()) {
            throw IllegalArgumentException("Password cannot be empty.")
        }

    }
}
