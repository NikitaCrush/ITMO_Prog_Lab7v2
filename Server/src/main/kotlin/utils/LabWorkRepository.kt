package utils

import data.*
import databaseManager.DatabaseManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.sql.Timestamp

/**
 * LabWorkRepository class provides a set of operations over LabWork objects.
 * It serves as a data access layer, interacting directly with the database.
 */
class LabWorkRepository : KoinComponent {

    private val databaseManager: DatabaseManager by inject()

    /**
     * Load all LabWork instances from the database.
     * @return The list of LabWork instances retrieved from the database.
     */
    fun loadLabWorks(): List<LabWork> {
        val labWorks = mutableListOf<LabWork>()
        val connection = databaseManager.connection
        val statement = connection?.createStatement()
        val resultSet = statement?.executeQuery("SELECT * FROM LabWorks2")

        while (resultSet?.next() == true) {
            val labWork = LabWork(
                id = resultSet.getLong("id"),
                name = resultSet.getString("name"),
                coordinates = Coordinates(
                    x = resultSet.getLong("coordinate_x"),
                    y = resultSet.getDouble("coordinate_y")
                ),
                creationDate = resultSet.getTimestamp("creation_date").toLocalDateTime(),
                minimalPoint = resultSet.getInt("minimalPoint"),
                personalQualitiesMinimum = resultSet.getInt("personalQualitiesMinimum"),
                difficulty = resultSet.getString("difficulty")?.let { Difficulty.valueOf(it) },
                discipline = Discipline(
                    name = resultSet.getString("discipline_name"),
                    selfStudyHours = resultSet.getLong("discipline_selfStudyHours")
                ),
                owner = resultSet.getString("owner")
            )

            labWorks.add(labWork)
        }

        return labWorks
    }

    /**
     * Add a LabWork instance to the database.
     * @param labWork A LabWork instance to be added.
     * @return The message indicating the result of the operation.
     */
    fun addLabWork(labWork: LabWork): String {
        val connection = databaseManager.connection
        val preparedStatement = connection?.prepareStatement("""
    INSERT INTO LabWorks2(
        id, 
        name, 
        coordinate_x, 
        coordinate_y, 
        creation_date, 
        minimalPoint, 
        personalQualitiesMinimum, 
        difficulty, 
        discipline_name, 
        discipline_selfStudyHours,
        owner  
    )
    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
""")

        preparedStatement?.setLong(1, labWork.id)
        preparedStatement?.setString(2, labWork.name)
        preparedStatement?.setLong(3, labWork.coordinates.x)
        preparedStatement?.setDouble(4, labWork.coordinates.y)
        preparedStatement?.setTimestamp(5, Timestamp.valueOf(labWork.creationDate))
        preparedStatement?.setInt(6, labWork.minimalPoint)
        preparedStatement?.setInt(7, labWork.personalQualitiesMinimum)
        preparedStatement?.setString(8, labWork.difficulty?.name)
        preparedStatement?.setString(9, labWork.discipline.name)
        preparedStatement?.setLong(10, labWork.discipline.selfStudyHours)
        preparedStatement?.setString(11, labWork.owner)

        val rowsAffected = preparedStatement?.executeUpdate() ?: 0

        if (rowsAffected > 0) {
            return Messages.LAB_WORK_SUCCESS_ADD
        } else {
            throw IllegalStateException("Failed to add LabWork to the database")
        }
    }

    /**
     * Remove a LabWork instance from the database by its id.
     * @param id The id of the LabWork to be removed.
     * @return The result of the operation.
     */
    fun removeLabWorkById(id: Long): Boolean {
        val connection = databaseManager.connection
        val preparedStatement = connection?.prepareStatement("DELETE FROM LabWorks2 WHERE id = ?")

        preparedStatement?.setLong(1, id)

        val rowsAffected = preparedStatement?.executeUpdate() ?: 0

        return rowsAffected > 0
    }

    /**
     * Clear all LabWork instances owned by a specific owner from the database.
     * @param owner The owner of the LabWork instances to be cleared.
     */
    fun clearLabWorksByOwner(owner: String) {
        val connection = databaseManager.connection
        val preparedStatement = connection?.prepareStatement("DELETE FROM LabWorks2 WHERE owner = ?")

        preparedStatement?.setString(1, owner)

        preparedStatement?.executeUpdate()
    }

    /**
     * Update a LabWork instance in the database.
     * @param updatedLabWork The new LabWork instance.
     * @return Whether the operation was successful.
     */
    fun updateLabWork(updatedLabWork: LabWork): Boolean {
        val connection = databaseManager.connection
        val preparedStatement = connection?.prepareStatement("""
            UPDATE LabWorks2 SET 
            name = ?, 
            coordinate_x = ?, 
            coordinate_y = ?, 
            minimalPoint = ?, 
            personalQualitiesMinimum = ?, 
            difficulty = ?, 
            discipline_name = ?, 
            discipline_selfStudyHours = ? 
            WHERE id = ?;
        """)

        preparedStatement?.setString(1, updatedLabWork.name)
        preparedStatement?.setLong(2, updatedLabWork.coordinates.x)
        preparedStatement?.setDouble(3, updatedLabWork.coordinates.y)
        preparedStatement?.setInt(4, updatedLabWork.minimalPoint)
        preparedStatement?.setInt(5, updatedLabWork.personalQualitiesMinimum)
        preparedStatement?.setString(6, updatedLabWork.difficulty?.name)
        preparedStatement?.setString(7, updatedLabWork.discipline.name)
        preparedStatement?.setLong(8, updatedLabWork.discipline.selfStudyHours)
        preparedStatement?.setLong(9, updatedLabWork.id)

        val rowsAffected = preparedStatement?.executeUpdate() ?: 0

        return rowsAffected > 0
    }
}
