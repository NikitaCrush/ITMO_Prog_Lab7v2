package utils

import data.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.ConcurrentSkipListSet
import java.time.LocalDate

/**
 * This class represents the service for managing lab work objects.
 *
 * @property labWorkRepository An instance of [LabWorkRepository] for managing lab works in the database.
 * @property labWorkSet A thread-safe set of lab work objects.
 */
class LabWorkService : KoinComponent {

    private val labWorkRepository: LabWorkRepository by inject()

    private val labWorkSet: ConcurrentSkipListSet<LabWork> = ConcurrentSkipListSet()

    /**
     * Load lab works from the repository to the concurrent set during the service initialization.
     */
    init {
        labWorkSet.addAll(labWorkRepository.loadLabWorks())
    }

    /**
     * Add a LabWork instance to the lab work set and repository.
     * @param labWork A LabWork instance to be added.
     * @return The message indicating the result of the operation.
     */
    fun add(labWork: LabWork): String {
        val message = labWorkRepository.addLabWork(labWork)
        labWorkSet.add(labWork)
        return message
    }

    /**
     * Remove a LabWork instance from the lab work set and repository by its id.
     * @param id The id of the LabWork to be removed.
     * @return The result of the operation.
     */
    fun removeById(id: Long): Boolean {
        val isRemoved = labWorkRepository.removeLabWorkById(id)
        if (isRemoved) {
            labWorkSet.removeIf { it.id == id }
        }
        return isRemoved
    }

    /**
     * Clear all LabWork instances owned by a specific owner from the lab work set and the repository.
     * @param owner The owner of the LabWork instances to be cleared.
     */
    fun clear(owner: String) {
        labWorkRepository.clearLabWorksByOwner(owner)
        labWorkSet.removeIf { it.owner == owner }
    }

    /**
     * Show all LabWork instances in the lab work set.
     * @return A list of LabWork instances.
     */
    fun show(): List<LabWork> {
        return labWorkSet.toList()
    }

    /**
     * Calculate the sum of minimalPoint for all LabWork instances in the lab work set.
     * @return The sum of minimalPoint.
     */
    fun sumOfMinimalPoint(): Int {
        return this.labWorkSet.sumOf { it.minimalPoint }
    }

    /**
     * Find the LabWork instance with the minimal difficulty in the lab work set.
     * @return The LabWork instance with the minimal difficulty.
     */
    fun minByDifficulty(): LabWork? {
        return labWorkSet.minByOrNull { it.difficulty ?: Difficulty.EASY }
    }

    /**
     * Generate a set of unique minimalPoint from all LabWork instances in the lab work set.
     * @return A set of unique minimalPoint.
     */
    fun printUniqueMinimalPoint(): Set<Int> {
        return labWorkSet.map { it.minimalPoint }.toSet()
    }

    /**
     * Remove the first LabWork instance owned by a specific owner from the lab work set and the repository.
     * @param owner The owner of the LabWork instance to be removed.
     * @return The LabWork instance that was removed.
     */
    fun removeFirst(owner: String): LabWork? {
        val labWork = labWorkSet.firstOrNull { it.owner == owner }
        if (labWork != null) {
            labWorkRepository.removeLabWorkById(labWork.id)
            labWorkSet.remove(labWork)
        }
        return labWork
    }

    /**
     * An alias for the removeFirst function.
     */
    fun removeHead(owner: String): LabWork? {
        return removeFirst(owner)
    }

    /**
     * Add a LabWork instance to the lab work set and the repository if it has the highest id among all LabWork instances owned by the same owner.
     * @param labWork The LabWork instance to be potentially added.
     * @return Whether the operation was successful.
     */
    fun addIfMax(labWork: LabWork): Boolean {
        if (labWorkSet.none { it.owner == labWork.owner }) return false

        val maxLabWork = labWorkSet.filter { it.owner == labWork.owner }.maxWithOrNull(compareBy { it.id })
        if (maxLabWork == null || compareBy<LabWork> { it.id }.compare(maxLabWork, labWork) < 0) {
            add(labWork)
            return true
        }
        return false
    }

    /**
     * Update a LabWork instance in the lab work set and the repository.
     * The owner of the new LabWork instance should be the same as the owner of the existing one.
     * @param id The id of the LabWork instance to be updated.
     * @param newLabWork The new LabWork instance.
     * @return Whether the operation was successful.
     */
    fun update(id: Long, newLabWork: LabWork): Boolean {
        val labWork = labWorkSet.find { it.id == id }

        if (labWork != null && labWork.owner == newLabWork.owner) {
            val updatedLabWork = LabWork(
                id = labWork.id,
                name = newLabWork.name,
                coordinates = newLabWork.coordinates,
                creationDate = labWork.creationDate,
                minimalPoint = newLabWork.minimalPoint,
                personalQualitiesMinimum = newLabWork.personalQualitiesMinimum,
                difficulty = newLabWork.difficulty,
                discipline = newLabWork.discipline,
                owner = labWork.owner
            )

            labWorkRepository.updateLabWork(updatedLabWork)
            labWorkSet.remove(labWork)
            labWorkSet.add(updatedLabWork)
            return true
        }
        return false
    }

    /**
     * Get information about the lab work set, including its type, initialization date, and the number of elements.
     * @return A string containing the information about the lab work set.
     */
    fun getInfo(): String {
        return "Collection type: ${labWorkSet::class.simpleName}\n" +
                "Initialization date: ${LocalDate.now()}\n" +
                "Number of elements: ${labWorkSet.size}"
    }
}
