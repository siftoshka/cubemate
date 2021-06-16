package az.siftoshka.cubemate.utils

import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class Converter {
    companion object {
        fun roundOffDecimal(number: Float): Float {
            val df = DecimalFormat("#.###")
            df.roundingMode = RoundingMode.CEILING
            return df.format(number).toFloat()
        }

        fun timeToDate(timestamp: Long): String {
            val calendar = Calendar.getInstance().apply { timeInMillis = timestamp }
            val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            return dateFormat.format(calendar.time)
        }
    }
}

fun Float.round(decimals: Int): String {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    val result = kotlin.math.round(this * multiplier) / multiplier
    return result.toString()
}