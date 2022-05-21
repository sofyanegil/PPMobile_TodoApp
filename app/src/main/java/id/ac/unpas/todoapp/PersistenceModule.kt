package id.ac.unpas.todoapp

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {
    @Provides
    @Singleton
    fun provideTodoDatabase(application: Application): TodoDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            TodoDatabase::class.java,
            "todo_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideTodoDAo(todoDatabase: TodoDatabase): TodoDao {
        return todoDatabase.todoDao()
    }
}