package dev.shtanko.androidlab.movies.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffoldLayout
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import dev.shtanko.androidlab.utils.MoviesNavigationContentPosition
import kotlinx.coroutines.launch

class MoviesNavSuiteScope(
    val navSuiteType: NavigationSuiteType,
)

@Composable
fun MoviesNavigationWrapper(
    currentDestination: NavDestination?,
    navigateToTopLevelDestination: (MoviesTopLevelDestination) -> Unit = {},
    content: @Composable MoviesNavSuiteScope.() -> Unit = {},
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val windowSize = with(LocalDensity.current) {
        currentWindowSize().toSize().toDpSize()
    }
    val navLayoutType = when {
        adaptiveInfo.windowPosture.isTabletop -> NavigationSuiteType.NavigationBar
        adaptiveInfo.windowSizeClass.isCompact() -> NavigationSuiteType.NavigationBar
        adaptiveInfo.windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED &&
            windowSize.width >= 1200.dp -> NavigationSuiteType.NavigationDrawer

        else -> NavigationSuiteType.NavigationRail
    }
    val navContentPosition = when (adaptiveInfo.windowSizeClass.windowHeightSizeClass) {
        WindowHeightSizeClass.COMPACT,
            -> MoviesNavigationContentPosition.TOP

        WindowHeightSizeClass.MEDIUM,
        WindowHeightSizeClass.EXPANDED,
            -> MoviesNavigationContentPosition.CENTER

        else -> MoviesNavigationContentPosition.TOP
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    // Avoid opening the modal drawer when there is a permanent drawer or a bottom nav bar,
    // but always allow closing an open drawer.
    val gesturesEnabled =
        drawerState.isOpen || navLayoutType == NavigationSuiteType.NavigationRail

    BackHandler(enabled = drawerState.isOpen) {
        coroutineScope.launch {
            drawerState.close()
        }
    }

    ModalNavigationDrawer(
        // drawerState = drawerState,
        gesturesEnabled = false,
        drawerContent = {
            ModalNavigationDrawerContent(
                currentDestination = currentDestination,
                navigationContentPosition = navContentPosition,
                navigateToTopLevelDestination = navigateToTopLevelDestination,
                onDrawerClicked = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                },
            )
        },
    ) {
        NavigationSuiteScaffoldLayout(
            layoutType = navLayoutType,
            navigationSuite = {
                when (navLayoutType) {
                    NavigationSuiteType.NavigationBar -> MoviesBottomNavigationBar(
                        currentDestination = currentDestination,
                        navigateToTopLevelDestination = navigateToTopLevelDestination,
                    )

                    NavigationSuiteType.NavigationRail -> MoviesNavigationRail(
                        currentDestination = currentDestination,
                        navigateToTopLevelDestination = navigateToTopLevelDestination,
                        onDrawerClicked = {
                            coroutineScope.launch {
                                drawerState.open()
                            }
                        },
                    )

                    NavigationSuiteType.NavigationDrawer -> PermanentNavigationDrawerContent(
                        currentDestination = currentDestination,
                        navigationContentPosition = navContentPosition,
                        navigateToTopLevelDestination = navigateToTopLevelDestination,
                    )
                }
            },
        ) {
            MoviesNavSuiteScope(navLayoutType).content()
        }
    }
}

@Preview(device = "id:pixel_9_pro", showBackground = true)
@Composable
private fun MoviesNavigationWrapperPhonePreview() {
    AndroidLabTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        MoviesNavigationWrapper(
            currentDestination = currentDestination,
        )
    }
}

@Preview(device = "id:pixel_9_pro_fold", showBackground = true)
@Composable
private fun MoviesNavigationWrapperFoldablePreview() {
    AndroidLabTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        MoviesNavigationWrapper(
            currentDestination = currentDestination,
        )
    }
}

@Preview(device = "id:medium_tablet", showBackground = true)
@Composable
private fun MoviesNavigationWrapperTabletPreview() {
    AndroidLabTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        MoviesNavigationWrapper(
            currentDestination = currentDestination,
        )
    }
}
