package utils

import kotlinx.serialization.KSerializer

/**
 * Object that manages work with file.
 */
object FileUtil {

    fun <T> loadFromFile(fileName: String, serializer: KSerializer<T>): T? {
        return JsonUtil.loadFromFile(fileName, serializer)
    }

    fun <T : Any> saveToFile(obj: T, fileName: String, serializer: KSerializer<T>) {
        JsonUtil.saveToFile(obj, fileName, serializer)
    }
}
