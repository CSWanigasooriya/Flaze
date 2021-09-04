package com.flaze.tracer

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.flaze.tracer.ui.theme.FlazeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current

            FlazeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    Main(context = context)
                }
            }
        }
    }
}

@Composable
fun Main(context: Context) {

    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    Scaffold(
        drawerContent = { ModalDrawer() },
        drawerShape = RoundedCornerShape(topEnd = 12.dp),
        scaffoldState = scaffoldState,
        topBar = { Toolbar(coroutineScope, scaffoldState) },
    ) {
        BodyContent(Modifier.fillMaxSize(), coroutineScope, scaffoldState)
    }
}


@Composable
fun Toolbar(coroutineScope: CoroutineScope, scaffoldState: ScaffoldState) {
    TopAppBar(
        title = { Text(text = "Toolbar") },
        navigationIcon = {
            // show drawer icon
            IconButton(
                onClick = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.open()
                    }
                }
            ) {
                Icon(Icons.Filled.Menu, contentDescription = "Drawer")
            }
        },
    )
}

@Composable
fun BodyContent(
    modifier: Modifier = Modifier,
    coroutineScope: CoroutineScope,
    scaffoldState: ScaffoldState
) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column {
            Text("Chamath")
            Text("Sasanka")
            Text("Wanigasooriya")
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                repeat(15) {
                    Button(onClick = {
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar("Hello!",duration = SnackbarDuration.Short,actionLabel = "Close")
                        }
                    }) {
                        Text(text = "Button")
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}

@Composable
fun ModalDrawer() {
    Column() {
        Text(text = "Drawer")
    }
}