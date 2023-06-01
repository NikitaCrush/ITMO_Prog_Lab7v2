package utils

/**
 * A utility object for generating unique IDs.
 */
object IdGenerator {
    private val generatedIds = mutableSetOf<Long>()

    /**
     * Generates a unique ID within the range of 1 to Long.MAX_VALUE.
     *
     * @return A unique ID.
     */
    fun generateUniqueId(): Long {
        var id: Long
        do {
            id = (1L..Long.MAX_VALUE).random()
        } while (generatedIds.contains(id))
        generatedIds.add(id)
        return id
    }
}

