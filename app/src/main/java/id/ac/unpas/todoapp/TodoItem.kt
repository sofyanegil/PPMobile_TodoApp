package id.ac.unpas.todoapp

import androidx.room.ColumnInfo
import  androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_list")
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val id : Long  = 0L,

    val name: String,

    @ColumnInfo(name = "is_done")
    var isDone: Boolean = false
)
