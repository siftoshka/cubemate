package az.siftoshka.cubemate.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Result::class], version = 1)
abstract class ResultDatabase : RoomDatabase() {

    abstract fun getResultDAO() : ResultDAO
}