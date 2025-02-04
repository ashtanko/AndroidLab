package dev.shtanko.androidlab.movies.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import dev.shtanko.androidlab.R
import kotlinx.serialization.Serializable

@Serializable
sealed class Route(val path: String) {
    @Serializable
    data object Home : Route("/home")

    @Serializable
    data object Search : Route("/search")

    @Serializable
    data object Favorites : Route("/fav")

    @Serializable
    data object Settings : Route("/settings")
}

data class MoviesTopLevelDestination(
    val route: Route,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int,
)

class MoviesNavigationActions(
    private val navController: NavHostController,
) {
    fun navigateTo(destination: MoviesTopLevelDestination) {
        navController.navigate(destination.route.path) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }
}

val TOP_LEVEL_DESTINATIONS = listOf(
    MoviesTopLevelDestination(
        route = Route.Home,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        iconTextId = R.string.tab_home,
    ),
    MoviesTopLevelDestination(
        route = Route.Search,
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Default.Search,
        iconTextId = R.string.tab_search,
    ),
    MoviesTopLevelDestination(
        route = Route.Favorites,
        selectedIcon = Icons.Filled.Star,
        unselectedIcon = Icons.Outlined.Star,
        iconTextId = R.string.tab_favorites,
    ),
    MoviesTopLevelDestination(
        route = Route.Settings,
        selectedIcon = Icons.Filled.Settings,
        unselectedIcon = Icons.Outlined.Settings,
        iconTextId = R.string.tab_settings,
    ),
)
