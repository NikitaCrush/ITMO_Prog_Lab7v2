package data

import kotlinx.serialization.Serializable

/**
 * A data class representing coordinates with x and y values.
 *
 * @property x The x coordinate value.
 * @property y The y coordinate value.
 */
@Serializable
data class Coordinates(
    val x: Long,
    val y: Double
)