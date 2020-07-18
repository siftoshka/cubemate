package az.siftoshka.cubemate.utils

object TypeConverter {

    fun convertToString(number: Int): String {
        return when (number) {
            3 -> "3x3"
            4 -> "4x4"
            5 -> "5x5"
            6 -> "6x6"
            else -> ""
        }
    }
}