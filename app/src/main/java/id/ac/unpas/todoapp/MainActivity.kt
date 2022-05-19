package id.ac.unpas.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import id.ac.unpas.todoapp.ui.theme.TodoAppTheme
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Column as Column1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoAppTheme {
                // A surface container using the 'background' color from the theme
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen(scaffoldState: ScaffoldState = rememberScaffoldState()) {
    val todoDatabase = TodoDatabase.getInstance(LocalContext.current)
    val todoRepository = TodoRepository(todoDao = todoDatabase.todoDao())
    val liveData = todoRepository.readAllData
    val items: List<TodoItem> by liveData.observeAsState(initial = listOf())
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Scaffold(scaffoldState = scaffoldState) {
            Column1(modifier = Modifier.padding(8.dp)) {
                OutlinedTextField(value = name.value.text,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    label = { Text(text = "Name") },
                    onValueChange = {
                        name.value = TextFieldValue(it)
                    })

                Button(onClick = {
                    scope.launch {
                        todoRepository.addTodo(TodoItem(0, name.value.text, false))
                        name.value = TextFieldValue("")
                        scaffoldState.snackbarHostState.showSnackbar("Activity has been saved")
                    }
                }) {
                    Text(text = "Save")
                }

                Divider(color = Color.Gray, thickness = 1.dp)

                TodoList(list = items)
            }

        }
    }
}

