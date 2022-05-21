package id.ac.unpas.todoapp.module

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import id.ac.unpas.todoapp.network.TodoService
import id.ac.unpas.todoapp.persistence.TodoDao
import id.ac.unpas.todoapp.repository.TodoRepository

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {
    @Provides
    @ViewModelScoped
    fun provideTodoRepository(dao: TodoDao, service: TodoService): TodoRepository {
        return TodoRepository(dao, service)
    }
}