package id.ac.unpas.todoapp

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TodoDao {
    @Query("select * from todo_list")
    fun getAll(): LiveData<List<TodoItem>>

    @Insert
    suspend fun insert(item: TodoItem)
}