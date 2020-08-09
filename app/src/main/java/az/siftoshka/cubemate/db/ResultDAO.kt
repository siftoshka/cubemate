package az.siftoshka.cubemate.db

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ResultDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: Result)

    @Delete
    suspend fun deleteResult(result: Result)

    @Query("DELETE FROM results_table")
    suspend fun deleteAllResults()

    @Query("SELECT * FROM results_table ORDER BY timeInSeconds ASC")
    fun getAllResultsByTime(): Flow<List<Result>>

    @Query("SELECT * FROM results_table ORDER BY timestamp DESC")
    fun getAllResultsByDate(): Flow<List<Result>>

    @Query("SELECT * FROM results_table ORDER BY typeNumber ASC")
    fun getAllResultsByType(): Flow<List<Result>>

    @Query("SELECT AVG(timeInSeconds) FROM results_table")
    fun getAvgResult(): Flow<Float>

    @Query("SELECT * FROM results_table ORDER BY timeInSeconds ASC LIMIT 1")
    fun getBestResult(): Flow<Result>

    @Query("SELECT * FROM results_table ORDER BY timestamp DESC LIMIT 1")
    fun getRecentResult(): Flow<Result>
}