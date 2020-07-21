package az.siftoshka.cubemate.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "results_table")
data class Result(
    var timeInSeconds: Float = 0f,
    var timestamp: Long = 0L,
    var type: String? = null,
    var typeNumber: Int = 0
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
