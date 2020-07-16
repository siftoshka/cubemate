package az.siftoshka.cubemate.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ResultDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: Result)

    @Delete
    suspend fun deleteResult(result: Result)

    @Query("SELECT * FROM results_table ORDER BY timeInMilliseconds DESC")
    fun getAllResults(): LiveData<List<Result>>

    @Query("SELECT AVG(timeInMilliseconds) FROM results_table")
    fun getAvgResult(): LiveData<Long>

    @Query("DELETE FROM results_table")
    suspend fun deleteAllResults()
}