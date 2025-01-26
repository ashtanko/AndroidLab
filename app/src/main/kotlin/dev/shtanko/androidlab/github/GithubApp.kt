package dev.shtanko.androidlab.github

import android.content.Context
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import dev.shtanko.androidlab.github.presentation.repositories.RepositoriesScreen
import dev.shtanko.androidlab.github.presentation.users.UsersScreen

@Composable
fun GithubApp(
    displayFeatures: List<DisplayFeature>,
    modifier: Modifier = Modifier,
    appState: GithubAppState = rememberGithubAppState(),
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()

    NavHost(
        modifier = modifier,
        navController = appState.navController,
        startDestination = GithubScreenRoutes.UsersScreen.route,
    ) {
        composable(GithubScreenRoutes.UsersScreen.route) { backStackEntry ->
            UsersScreen(
                modifier = Modifier,
                windowSizeClass = adaptiveInfo.windowSizeClass,
                onUserClick = {
                    appState.navigateToRepositories(username = it, backStackEntry)
                },
            )
        }
        composable(GithubScreenRoutes.RepositoriesScreen.route) {
            RepositoriesScreen(
                modifier = Modifier,
                navigateBack = {
                    appState.navigateBack()
                },
            )
        }
    }
}

sealed class GithubScreenRoutes(val route: String) {
    object UsersScreen : GithubScreenRoutes(route = "users_screen")
    object RepositoriesScreen : GithubScreenRoutes(route = "repositories_screen/{$ARG_USERNAME}") {
        fun createRoute(username: String) = "repositories_screen/$username"
    }

    companion object {
        const val ARG_USERNAME = "username"
    }
}

@Composable
fun rememberGithubAppState(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current,
) = remember(navController, context) {
    GithubAppState(
        navController = navController,
        context = context,
    )
}

class GithubAppState(
    val navController: NavHostController,
    private val context: Context,
) {
    fun navigateToRepositories(username: String, from: NavBackStackEntry) {
        if (from.lifecycleIsResumed()) {
            navController.navigate(GithubScreenRoutes.RepositoriesScreen.createRoute(username))
        }
    }

    fun navigateBack() {
        navController.popBackStack()
    }
}

private fun NavBackStackEntry.lifecycleIsResumed() =
    this.getLifecycle().currentState == Lifecycle.State.RESUMED
