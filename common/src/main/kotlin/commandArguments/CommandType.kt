package commandArguments

import kotlinx.serialization.Serializable

@Serializable
enum class CommandType {
    NO_ARG,
    SINGLE_ARG,
    LABWORK_ARG,
    ARG_AND_LABWORK
}