package utils

import data.Difficulty
import data.LabWork
import java.time.LocalDate
import java.util.PriorityQueue
import kotlinx.serialization.builtins.ListSerializer

/**
 * Class responsible for managing the LabWork collection.
 *
 * @property fileName The file name to use for loading and saving the LabWork collection.
 */
class LabWorkCollection(private val fileName: String) {
    private val labWorkQueue = PriorityQueue<LabWork>()

    init {
        System.err.println("LabWorkCollection created")
        loadFromFile()
    }

    /**
     * Loads the LabWork collection from the file.
     */
    private fun loadFromFile() {
        try {
            val labWorkList = FileUtil.loadFromFile(fileName, ListSerializer(LabWork.serializer()))
            labWorkList?.let { labWorkQueue.addAll(it) }
        } catch (e: kotlinx.serialization.SerializationException) {
            println("Error: Failed to load data from file. The file might be empty or contain invalid content. Starting with an empty collection.")
        }
    }

    /**
     * Saves the LabWork collection to the file.
     */
    fun saveToFile() {
        FileUtil.saveToFile(labWorkQueue.toList(), fileName, ListSerializer(LabWork.serializer()))
    }




    /**
     * Adds a LabWork object to the collection.
     *
     * @param labWork The LabWork object to add.
     */
    fun add(labWork: LabWork) {
        labWorkQueue.add(labWork)
    }

    /**
     * Removes a LabWork object from the collection by its ID.
     *
     * @param id The ID of the LabWork object to remove.
     * @return True if the object was removed, false otherwise.
     */
    fun removeById(id: Long): Boolean {
        val initialSize = labWorkQueue.size
        labWorkQueue.removeIf { it.id == id }
        return labWorkQueue.size < initialSize
    }


    /**
     * Clears the collection of all LabWork objects.
     */
    fun clear() {
        labWorkQueue.clear()
    }

    /**
     * Returns a list of all LabWork objects in the collection.
     *
     * @return A list of LabWork objects.
     */
    fun show(): List<LabWork> {
        return labWorkQueue.toList()
    }

    /**
     * Returns the number of LabWork objects in the collection.
     *
     * @return The size of the collection.
     */
    private fun size(): Int {
        return labWorkQueue.size
    }

    /**
     * Returns the current date as a LocalDate object.
     *
     * @return The current date.
     */
    private fun getCreationDate(): LocalDate {
        return LocalDate.now()
    }

    /**
     * Removes the first LabWork object in the collection.
     */
    fun removeFirst() {
        labWorkQueue.poll()
    }

    /**
     * Calculates the sum of the minimal points of all LabWork objects in the collection.
     *
     * @return The sum of minimal points.
     */
    fun sumOfMinimalPoint(): Int {
        return this.labWorkQueue.sumOf { it.minimalPoint }
    }

    /**
     * Returns the LabWork object with the lowest difficulty in the collection.
     *
     * @return The LabWork object with the lowest difficulty or null if the collection is empty.
     */
    fun minByDifficulty(): LabWork? {
        return labWorkQueue.minByOrNull { it.difficulty ?: Difficulty.EASY }
    }

    /**
     * Returns a set of unique minimal point values in the collection.
     *
     * @return A set of unique minimal point values.
     */
    fun printUniqueMinimalPoint(): Set<Int> {
        return labWorkQueue.map { it.minimalPoint }.toSet()
    }

    /**
     * Adds a LabWork object to the collection if its ID is greater than the current maximum ID.
     *
     * @param labWork The LabWork object to add.
     * @return True if the object was added, false otherwise.
     */
    fun addIfMax(labWork: LabWork): Boolean {
        val maxLabWork = labWorkQueue.maxWithOrNull(compareBy { it.id })
        if (maxLabWork == null || compareBy<LabWork> { it.id }.compare(maxLabWork, labWork) < 0) {
            labWorkQueue.add(labWork)
            return true
        }
        return false
    }

    /**
     * Removes and returns the first LabWork object in the collection.
     *
     * @return The removed LabWork object or null if the collection is empty.
     */
    fun removeHead(): LabWork? {
        return labWorkQueue.poll()
    }

    /**
     * Returns information about the collection, including its type, initialization date, and number of elements.
     *
     * @return A string containing the collection information.
     */
    fun getInfo(): String {
        return "Collection type: ${labWorkQueue::class.simpleName}\n" +
                "Initialization date: ${getCreationDate()}\n" +
                "Number of elements: ${size()}"
    }

    /**
     * Updates a LabWork object in the collection with the provided ID and new data.
     *
     * @param id The ID of the LabWork object to update.
     * @param newLabWork The new LabWork object containing the updated data.
     * @return True if the object was updated, false otherwise.
     */
    fun update(id: Long, newLabWork: LabWork): Boolean {
        val labWork = labWorkQueue.find { it.id == id }
        return if (labWork != null) {
            val updatedLabWork = LabWork(
                id = labWork.id,
                name = newLabWork.name,
                coordinates = newLabWork.coordinates,
                creationDate = labWork.creationDate,
                minimalPoint = newLabWork.minimalPoint,
                personalQualitiesMinimum = newLabWork.personalQualitiesMinimum,
                difficulty = newLabWork.difficulty,
                discipline = newLabWork.discipline
            )

            labWorkQueue.remove(labWork)
            labWorkQueue.add(updatedLabWork)

            true
        } else {
            false
        }
    }

}
