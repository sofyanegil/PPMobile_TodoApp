package id.ac.unpas.todoapp.ui.main

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import id.ac.unpas.todoapp.entity.TodoItem
import id.ac.unpas.todoapp.ui.theme.TodoAppTheme
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Column as Column1

@AndroidEntryPoint
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
    val mainViewModel = hiltViewModel<MainViewModel>()
    val items: List<TodoItem> by mainViewModel.liveData.observeAsState(initial = listOf())
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Scaffold(scaffoldState = scaffoldState) {
            Column1(modifier = Modifier.padding(8.dp)) {
                OutlinedTextField(
                    value = name.value.text,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    label = { Text(text = "Name") },
                    onValueChange = {
                        name.value = TextFieldValue(it)
                    })

                Button(onClick = {
                    scope.launch {
                        mainViewModel.addTodo(name.value.text)
                        name.value = TextFieldValue("")
                        scaffoldState.snackbarHostState.showSnackbar("Activity has been saved")
                    }
                }) {
                    Text(text = "Save")
                }

                Button(onClick = {
                    scope.launch {
                        mainViewModel.syncTodo()
                    }
                }) {
                    Text(text = "Refresh")
                }

                Divider(color = Color.Gray, thickness = 1.dp)

                TodoList(list = items)
            }

        }
    }
}
