package commandArguments

import kotlinx.serialization.Serializable

/**
 * Enum class for the types of command.
 */
@Serializable
enum class CommandType {
    NO_ARG,
    SINGLE_ARG,
    LABWORK_ARG,
    ARG_AND_LABWORK,
    USER_REGISTRATION,
    USER_LOGIN,
    USER_LOGOUT
}