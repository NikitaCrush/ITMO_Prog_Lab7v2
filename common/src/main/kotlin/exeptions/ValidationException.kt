package exeptions

/**
 * Exception class for invalid input values.
 *
 * @param message The error message to be displayed.
 */
class ValidationException(message: String) : Exception(message)
