package utils

import data.Coordinates
import data.Discipline
import exeptions.ValidationException

/**
 * Class responsible for validating the input data for LabWork instances.
 */
class Validator {
    /**
     * Validates the name of a LabWork instance.
     *
     * @param name The name to validate.
     * @throws ValidationException If the name is empty or null.
     */
    fun validateName(name: String?) {
        if (name.isNullOrBlank()) {
            throw ValidationException("Name cannot be empty.")
        }
    }
    /**
    *
    * Validates the coordinates of a LabWork instance.
    * @param coordinates The coordinates to validate.
    * @throws ValidationException If the coordinates are null or the X value is greater than 608.
     */
    fun validateCoordinates(coordinates: Coordinates?) {
        if (coordinates == null || coordinates.x > 608) {
            throw ValidationException("X coordinate value cannot be greater than 608.")
        }
    }

    /**

    * Validates the minimal point value of a LabWork instance.
    * @param minimalPoint The minimal point value to validate.
    * @throws ValidationException If the minimal point value is null or less than or equal to 0.
    */
    fun validateMinimalPoint(minimalPoint: Int?) {
        if (minimalPoint == null || minimalPoint <= 0) {
            throw ValidationException("Minimal point value must be greater than 0.")
        }
    }

    /**

    * Validates the personal qualities minimum value of a LabWork instance.
    * @param personalQualitiesMinimum The personal qualities minimum value to validate.
    * @throws ValidationException If the personal qualities minimum value is null or less than or equal to 0.
    */
    fun validatePersonalQualitiesMinimum(personalQualitiesMinimum: Int?) {
        if (personalQualitiesMinimum == null || personalQualitiesMinimum <= 0) {
            throw ValidationException("Personal qualities minimum value must be greater than 0.")
        }
    }

//    fun validateDifficulty(difficulty: Difficulty?) {
//        // No validation required as the field can be null
//    }

    /**

    * Validates the discipline of a LabWork instance.
    * @param discipline The discipline to validate.
    * @throws ValidationException If the discipline is null or the name is empty.
    */
    fun validateDiscipline(discipline: Discipline?) {
        if (discipline == null || discipline.name.isBlank()) {
            throw ValidationException("Discipline name cannot be empty.")
        }
    }

    /**

    * Validates the self-study hours value of a LabWork instance.
    * @param selfStudyHours The self-study hours value to validate.
    * @throws ValidationException If the self-study hours value is null or less than 1.
    */
    fun validateSelfStudyHours(selfStudyHours: Long?) {
        if (selfStudyHours == null || selfStudyHours < 1) {
            throw ValidationException("Self-study hours must be a non-negative value and not 0.")
        }
    }
}
