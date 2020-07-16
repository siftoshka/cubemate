package az.siftoshka.cubemate.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "results_table")
data class Result(
    var timeInMilliseconds: Long = 0L,
    var type: String? = null
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}
