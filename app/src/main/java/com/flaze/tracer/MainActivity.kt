package com.flaze.tracer

import android.graphics.drawable.VectorDrawable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.flaze.tracer.ui.theme.FlazeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


sealed class Screens(val route: String, @StringRes val resourceId: Int,val icon:ImageVector) {
    object HomeScreen : Screens("Home", R.string.home,icon = Icons.Filled.Home)
    object SearchScreen : Screens("Search", R.string.search,icon = Icons.Filled.Search)
}

private val screens = listOf(
    Screens.HomeScreen, Screens.SearchScreen
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FlazeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    elevation = 2.dp,
                    color = MaterialTheme.colors.surface,
                ) {
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screens.HomeScreen.route) {
        composable(Screens.HomeScreen.route) { HomeScreen(navController) }
        composable(Screens.SearchScreen.route) { SearchScreen(navController) }
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    val coroutineScope: CoroutineScope = rememberCoroutineScope()
    val scaffoldState: ScaffoldState = rememberScaffoldState()

    Scaffold(
        drawerContent = { Drawer() },
        drawerShape = RoundedCornerShape(topEnd = 12.dp),
        scaffoldState = scaffoldState,
        topBar = { Toolbar(coroutineScope, scaffoldState) },
        bottomBar = { BottomBar(navController = navController) }
    ) {
        BodyContent(Modifier.fillMaxSize(), coroutineScope, scaffoldState)
    }
}

@Composable
fun SearchScreen(navController: NavController){
    Column(verticalArrangement = Arrangement.Center) {
        Text(text = "Search")
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
        Text("Chamath")
        Text("Sasanka")
        Text("Wanigasooriya")

        repeat(15) {
            Button(onClick = {
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar(
                        "Hello!",
                        duration = SnackbarDuration.Short,
                        actionLabel = "Close"
                    )
                }
            }) {
                Text(text = "Button")
            }
            Spacer(modifier = Modifier.padding(8.dp))
        }
    }
}

@Composable
fun Drawer(
    modifier: Modifier = Modifier
) {
    Column(
        modifier
            .fillMaxSize()
    ) {
        Header()
    }
}


@Composable
fun Header() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(painter = rememberImagePainter(
            data = "https://avatars.githubusercontent.com/u/53285026?v=4",
            onExecute = { _, _ -> true },
            builder = {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_background)
                transformations(CircleCropTransformation())
            }
        ),
            contentDescription = null,
            modifier = Modifier
                .size(96.dp)
                .padding(12.dp)
                .shadow(elevation = 12.dp, shape = CircleShape))
        Column(modifier = Modifier.padding(end = 12.dp)) {
            Text(
                text = "Chamath Wanigasooriya",
                maxLines = 1,
                style = MaterialTheme.typography.body1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "chamathwanigasooriya@gmail.com",
                maxLines = 1,
                style = MaterialTheme.typography.caption,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
    Divider()
}


@Composable
fun BottomBar(navController: NavController) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        screens.forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = null) },
                label = { Text(stringResource(screen.resourceId)) },
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    navController.navigate(screen.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // re-selecting the same item
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item
                        restoreState = true
                    }
                }
            )
        }
    }
}