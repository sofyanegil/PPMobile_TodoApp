package id.ac.unpas.todoapp

import androidx.room.ColumnInfo
import  androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "todo_list")
data class TodoItem(
    @PrimaryKey
    val id : String,

    val name: String,

    @ColumnInfo(name = "is_done")
    @SerializedName("is_done")
    var isDone: Boolean = false
)
