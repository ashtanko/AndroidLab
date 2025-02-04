package dev.shtanko.androidlab.movies

import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import dev.shtanko.androidlab.movies.navigation.MoviesNavigationActions
import dev.shtanko.androidlab.movies.navigation.MoviesNavigationWrapper
import dev.shtanko.androidlab.movies.navigation.Route
import dev.shtanko.androidlab.ui.shared.EmptyComingSoon
import dev.shtanko.androidlab.utils.DevicePosture
import dev.shtanko.androidlab.utils.MoviesContentType
import dev.shtanko.androidlab.utils.MoviesNavigationType
import dev.shtanko.androidlab.utils.isBookPosture
import dev.shtanko.androidlab.utils.isSeparating

private fun NavigationSuiteType.toMoviesNavType() = when (this) {
    NavigationSuiteType.NavigationBar -> MoviesNavigationType.BOTTOM_NAVIGATION
    NavigationSuiteType.NavigationRail -> MoviesNavigationType.NAVIGATION_RAIL
    NavigationSuiteType.NavigationDrawer -> MoviesNavigationType.PERMANENT_NAVIGATION_DRAWER
    else -> MoviesNavigationType.BOTTOM_NAVIGATION
}

@Composable
fun MoviesApp(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
    modifier: Modifier = Modifier,
    navigateToDetail: (Long, MoviesContentType) -> Unit = { _, _ -> },
) {
    val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()

    val foldingDevicePosture = when {
        isBookPosture(foldingFeature) ->
            DevicePosture.BookPosture(foldingFeature.bounds)

        isSeparating(foldingFeature) ->
            DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)

        else -> DevicePosture.NormalPosture
    }

    val contentType = when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> MoviesContentType.SINGLE_PANE
        WindowWidthSizeClass.Medium -> if (foldingDevicePosture != DevicePosture.NormalPosture) {
            MoviesContentType.DUAL_PANE
        } else {
            MoviesContentType.SINGLE_PANE
        }

        WindowWidthSizeClass.Expanded -> MoviesContentType.DUAL_PANE
        else -> MoviesContentType.SINGLE_PANE
    }

    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        MoviesNavigationActions(navController)
    }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Surface {
        MoviesNavigationWrapper(
            currentDestination = currentDestination,
            navigateToTopLevelDestination = navigationActions::navigateTo,
        ) {
            MoviesNavHost(
                navController = navController,
                contentType = contentType,
                displayFeatures = displayFeatures,
                navigationType = navSuiteType.toMoviesNavType(),
                navigateToDetail = navigateToDetail,
            )
        }
    }
}

@Composable
private fun MoviesNavHost(
    navController: NavHostController,
    contentType: MoviesContentType,
    displayFeatures: List<DisplayFeature>,
    navigationType: MoviesNavigationType,
    navigateToDetail: (Long, MoviesContentType) -> Unit,
    modifier: Modifier = Modifier,
    closeDetailScreen: () -> Unit = {},
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Route.Home.path,
    ) {
        composable(Route.Home.path) {
            EmptyComingSoon()
        }
        composable(Route.Search.path) {
            EmptyComingSoon()
        }
        composable(Route.Favorites.path) {
            EmptyComingSoon()
        }
        composable(Route.Settings.path) {
            EmptyComingSoon()
        }
    }
}
