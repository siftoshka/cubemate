package az.siftoshka.cubemate.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ResultDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: Result)

    @Delete
    suspend fun deleteResult(result: Result)

    @Query("SELECT * FROM results_table ORDER BY timeInSeconds DESC")
    fun getAllResults(): LiveData<List<Result>>

    @Query("SELECT AVG(timeInSeconds) FROM results_table")
    fun getAvgResult(): LiveData<Float>

    @Query("SELECT * FROM results_table ORDER BY timeInSeconds ASC LIMIT 1")
    fun getBestResult(): LiveData<Result>

    @Query("SELECT * FROM results_table ORDER BY timestamp DESC LIMIT 1")
    fun getRecentResult(): LiveData<Result>

    @Query("DELETE FROM results_table")
    suspend fun deleteAllResults()
}