package id.ac.unpas.todoapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TodoList(list: List<TodoItem>, modifier: Modifier = Modifier) {
    val listState = rememberLazyListState()

    Column {
        LazyColumn(state = listState) {
            items(
                items = list,
                itemContent = { item ->
                    TodoListItem(item = item)
                }
            )
        }
    }
}