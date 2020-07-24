package az.siftoshka.cubemate.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import az.siftoshka.cubemate.db.ResultDatabase
import az.siftoshka.cubemate.utils.Constants.RESULTS_DATABASE_NAME
import az.siftoshka.cubemate.utils.Constants.SETTINGS_PREFERENCES
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideResultDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, ResultDatabase::class.java, RESULTS_DATABASE_NAME)
            .build()

    @Singleton
    @Provides
    fun provideResultDAO(database: ResultDatabase) = database.getResultDAO()

    @Singleton
    @Provides
    fun provideSettingsPreferences(@ApplicationContext app: Context): SharedPreferences =
        app.getSharedPreferences(SETTINGS_PREFERENCES, MODE_PRIVATE)
}