package id.ac.unpas.todoapp.ui.main

import androidx.annotation.WorkerThread
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import id.ac.unpas.todoapp.entity.TodoItem
import id.ac.unpas.todoapp.repository.TodoRepository
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val todoRepository: TodoRepository) : ViewModel() {
    val liveData = todoRepository.readAllData

    @WorkerThread
    suspend fun addTodo(name: String) {
        todoRepository.addTodo(TodoItem("", name, false))
    }

    suspend fun syncTodo() {
        todoRepository.sync()
    }
}