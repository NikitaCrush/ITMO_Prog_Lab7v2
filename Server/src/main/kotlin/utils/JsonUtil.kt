package utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.io.File

/**
 * A utility object for working with JSON files.
 */
object JsonUtil {
    private val json = Json {
        ignoreUnknownKeys = true
        prettyPrint = true // Add this line to enable pretty-printing
    }
    /**
     * Deserializes a JSON string into an object of the specified type.
     *
     * @param jsonString The JSON string to deserialize.
     * @param serializer The serializer for the object type.
     * @return The deserialized object or null if deserialization fails.
     */
    private fun <T> fromJson(jsonString: String, serializer: KSerializer<T>): T? {
        return try {
            json.decodeFromString(serializer, jsonString)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Loads an object of the specified type from a JSON file.
     *
     * @param fileName The name of the JSON file to load.
     * @param serializer The serializer for the object type.
     * @return The loaded object or null if loading fails.
     */
    fun <T> loadFromFile(fileName: String, serializer: KSerializer<T>): T? {
        return try {
            val jsonString = File(fileName).readText()
            fromJson(jsonString, serializer)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Saves an object of the specified type to a JSON file.
     *
     * @param obj The object to save.
     * @param fileName The name of the JSON file to save the object to.
     * @param serializer The serializer for the object type.
     */
    fun <T> saveToFile(obj: T, fileName: String, serializer: KSerializer<T>) {
        try {
            val jsonString = json.encodeToString(serializer, obj)
            File(fileName).writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
