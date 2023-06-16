package data

import kotlinx.serialization.Serializable

/**
 * A data class representing a discipline with a name and self-study hours.
 *
 * @property name The name of the discipline.
 * @property selfStudyHours The number of self-study hours for the discipline.
 */
@Serializable
data class Discipline(
    val name: String,//Поле не может быть null, Строка не может быть пустой
    val selfStudyHours: Long
)