package dev.shtanko.androidlab.github

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
) {
    val adaptiveInfo = currentWindowAdaptiveInfo()
    val navController = rememberNavController()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = GithubScreenRoutes.UsersScreen.route,
    ) {
        composable(GithubScreenRoutes.UsersScreen.route) {
            UsersScreen(
                modifier = Modifier,
                windowSizeClass = adaptiveInfo.windowSizeClass,
                onUserClick = {
                    navController.navigate(GithubScreenRoutes.RepositoriesScreen.route)
                },
            )
        }
        composable(GithubScreenRoutes.RepositoriesScreen.route) {
            RepositoriesScreen(
                modifier = Modifier,
                navigateBack = {
                    navController.popBackStack()
                },
            )
        }
    }
}

sealed class GithubScreenRoutes(val route: String) {
    object UsersScreen : GithubScreenRoutes(route = "users_screen")
    object RepositoriesScreen : GithubScreenRoutes(route = "repositories_screen")
}
