package utils

import data.*
import databaseManager.DatabaseManager
import java.sql.Timestamp
import java.time.LocalDate
import java.util.concurrent.ConcurrentSkipListSet

class LabWorkCollection {
    private val labWorkSet = ConcurrentSkipListSet<LabWork>()
    private val databaseManager = DatabaseManager()

    init {
        System.err.println("LabWorkCollection created")
        loadFromDatabase()
    }

    private fun loadFromDatabase() {
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

            labWorkSet.add(labWork)
        }

    }
//eyJhbGciOiJIUzI1NiJ9.eyJ1c2VybmFtZSI6InZydnIiLCJleHAiOjE2ODYxNjkzOTh9.m6Anf-Pce1ZYxe9zN4Bl1eYMFzvOaLG9q4cXojpILQg

    fun add(labWork: LabWork): String {

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

        // Assign owner to labWork object

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
            labWorkSet.add(labWork)  // LabWork object already includes owner
            return Messages.LAB_WORK_SUCCESS_ADD
        } else {
            throw IllegalStateException("Failed to add LabWork to the database")
        }
    }





    fun removeById(id: Long): Boolean {
        val labWork = labWorkSet.find { it.id == id }
        if (labWork != null) {
            val connection = databaseManager.connection
            val preparedStatement = connection?.prepareStatement("DELETE FROM LabWorks2 WHERE id = ?")

            preparedStatement?.setLong(1, id)

            val rowsAffected = preparedStatement?.executeUpdate() ?: 0

            if (rowsAffected > 0) {
                return labWorkSet.removeIf { it.id == id }
            }
        }
        throw IllegalArgumentException("Cannot remove a lab work that you did not create")
    }

    fun clear(token: String) {
        val connection = databaseManager.connection
        val preparedStatement = connection?.prepareStatement("DELETE FROM LabWorks2 WHERE owner = ?")

        preparedStatement?.setString(1, token)

        preparedStatement?.executeUpdate()

        labWorkSet.removeIf { it.owner == token }
    }

    fun show(): List<LabWork> {
        return labWorkSet.toList()
    }

    private fun size(): Int {
        return labWorkSet.size
    }

    private fun getCreationDate(): LocalDate {
        return LocalDate.now()
    }

    fun removeFirst(token: String) {
        val labWork = labWorkSet.firstOrNull { it.owner == token }
        if (labWork != null) {
            val connection = databaseManager.connection
            val preparedStatement = connection?.prepareStatement("DELETE FROM LabWorks2 WHERE id = ?")

            preparedStatement?.setLong(1, labWork.id)

            preparedStatement?.executeUpdate()

            labWorkSet.remove(labWork)
        }
    }

    fun sumOfMinimalPoint(): Int {
        return this.labWorkSet.sumOf { it.minimalPoint }
    }

    fun minByDifficulty(): LabWork? {
        return labWorkSet.minByOrNull { it.difficulty ?: Difficulty.EASY }
    }

    fun printUniqueMinimalPoint(): Set<Int> {
        return labWorkSet.map { it.minimalPoint }.toSet()
    }

    fun addIfMax(labWork: LabWork): Boolean {
        if (labWorkSet.none { it.owner == labWork.owner }) return false

        val maxLabWork = labWorkSet.filter { it.owner == labWork.owner }.maxWithOrNull(compareBy { it.id })
        if (maxLabWork == null || compareBy<LabWork> { it.id }.compare(maxLabWork, labWork) < 0) {
            add(labWork)  // Make sure to catch potential IllegalStateException here
            return true
        }
        return false
    }

    fun removeHead(token: String): LabWork? {
        val labWork = labWorkSet.firstOrNull { it.owner == token }
        if (labWork != null) {
            val connection = databaseManager.connection
            val preparedStatement = connection?.prepareStatement("DELETE FROM LabWorks2 WHERE id = ?")

            preparedStatement?.setLong(1, labWork.id)

            preparedStatement?.executeUpdate()

            labWorkSet.remove(labWork)
            return labWork
        }
        return null
    }

    fun getInfo(): String {
        return "Collection type: ${labWorkSet::class.simpleName}\n" +
                "Initialization date: ${getCreationDate()}\n" +
                "Number of elements: ${size()}"
    }

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

            if (rowsAffected > 0) {
                labWorkSet.remove(labWork)
                labWorkSet.add(updatedLabWork)
                return true
            }
        }
        throw IllegalArgumentException("Cannot update a lab work that you did not create")
    }

}
