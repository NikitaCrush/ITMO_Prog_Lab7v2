package data

import kotlinx.serialization.Serializable

/**
 * An enum class representing the difficulty levels of lab works.
 */
@Serializable
enum class Difficulty{
    EASY,
    NORMAL,
    TERRIBLE
}