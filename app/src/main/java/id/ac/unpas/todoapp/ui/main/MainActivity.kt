package id.ac.unpas.todoapp.ui.main

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import id.ac.unpas.todoapp.entity.TodoItem
import id.ac.unpas.todoapp.ui.theme.TodoAppTheme
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
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

@ExperimentalPermissionsApi
@Composable
fun MainScreen() {
    val context = LocalContext.current
    var doNotShowRationale by rememberSaveable {
        mutableStateOf(false)
    }
    var permissionState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    when {
        permissionState.allPermissionsGranted -> {
            MainScreenContent(locationPermitted = true, fusedLocationClient = fusedLocationClient)
        }
        permissionState.shouldShowRationale || !permissionState.permissionRequested -> {
            Column() {
                Text(text = "Aplikasi membutuhkan izin untuk mengakses lokasi anda")
                Button(onClick = { permissionState.launchMultiplePermissionRequest()}) {
                    Text(text = "Minta izin")
                }
                Button(onClick = { doNotShowRationale = false}) {
                    Text(text = "Jangan munculkan lagi")
                }
            }
        }
        else -> {
            MainScreenContent(locationPermitted = false, fusedLocationClient = fusedLocationClient )
        }
    }
}


@SuppressLint("MissingPermission")
@Composable
fun MainScreenContent(
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    locationPermitted: Boolean,
    fusedLocationClient: FusedLocationProviderClient
) {
    val mainViewModel = hiltViewModel<MainViewModel>()
    val items: List<TodoItem> by mainViewModel.liveData.observeAsState(initial = listOf())
    val name = remember { mutableStateOf(TextFieldValue("")) }
    val latitude = remember { mutableStateOf(TextFieldValue("")) }
    val longitude = remember { mutableStateOf(TextFieldValue("")) }
    val scope = rememberCoroutineScope()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Scaffold(scaffoldState = scaffoldState) {
            Column(modifier = Modifier.padding(8.dp)) {
                OutlinedTextField(
                    value = name.value.text,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    label = { Text(text = "Name") },
                    onValueChange = {
                        name.value = TextFieldValue(it)
                    })

                if (locationPermitted) {
                    OutlinedTextField(
                        value = latitude.value.text,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        label = { Text(text = "Latitude") },
                        onValueChange = {
                            latitude.value = TextFieldValue(it)
                        })

                    OutlinedTextField(
                        value = longitude.value.text,
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        label = { Text(text = "Longitude") },
                        onValueChange = {
                            longitude.value = TextFieldValue(it)
                        })

                }

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
                        if(locationPermitted){
                            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                                location?.let {
                                    latitude.value = TextFieldValue(it.latitude.toString())
                                    longitude.value = TextFieldValue(location.longitude.toString())
                                }
                            }
                        }
                        // mainViewModel.syncTodo()
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
