package id.ac.unpas.todoapp

import androidx.lifecycle.LiveData

class TodoRepository(private val todoDao: TodoDao) {
    val readAllData: LiveData<List<TodoItem>> = todoDao.getAll()

    suspend fun addTodo(todoItem: TodoItem){
        todoDao.insert(todoItem)
    }
}