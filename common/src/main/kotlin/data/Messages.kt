package data

/**
 * An object that holds constant message strings for user prompts and errors.
 */
object Messages {
    const val REGISTRATION_SUCCESS = "  REGISTRATION_SUCCESS"
    const val REGISTRATION_FAIL = "registration_fail"
    const val LOGIN_FAIL = "login_fail"
    const val WELCOME = "Welcome to the LabWork Manager!"
    const val ENTER_HELP = "Enter 'help' for the list of available commands."
    const val ENTER_NAME = "Enter name: "
    const val ENTER_X = "Enter coordinates x (Long): "
    const val ENTER_Y = "Enter coordinates y (Double): "
    const val INVALID_COORDINATES = "Invalid input. Please enter valid coordinates."
    const val ENTER_MINIMAL_POINT = "Enter minimalPoint: "
    const val ENTER_DIFFICULTY = "Enter difficulty (EASY, NORMAL, TERRIBLE) or leave it empty for null: "
    const val INVALID_DIFFICULTY = "Invalid input. Please enter a valid difficulty (EASY, NORMAL, TERRIBLE) or leave it empty."
    const val ENTER_DISCIPLINE = "Enter discipline name: "
    const val INVALID_DISCIPLINE = "Invalid input. Please enter a valid discipline (name selfStudyHours(Long))."
    const val ENTER_PERSONAL_QUALITIES_MIN = "Enter personalQualitiesMinimum: "
    const val INVALID_NUMBER = "Invalid input. Please enter a valid number."
    const val ENTER_SELF_STUDY_HOURS = "Enter selfStudyHours: "
    const val LAB_WORK_SUCCESS_ADD = "Lab work added successfully."
    const val LAB_WORK_SUCCESS_CLEAR = "Lab work collection cleared successfully."
//    const val LAB_WORK_SUCCESS_SAVE = "Lab work collection saved successfully."
    const val LAB_WORK_NOT_MAX = "The element is not the maximum and was not added to the collection."
    const val LOGOUT_SUCCESS = "Logged out successfully."

}
