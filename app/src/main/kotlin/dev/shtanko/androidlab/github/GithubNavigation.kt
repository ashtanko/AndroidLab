@file:OptIn(ExperimentalComposeUiApi::class)

package dev.shtanko.androidlab.github

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.shtanko.androidlab.github.presentation.repositories.RepositoriesScreen
import dev.shtanko.androidlab.github.presentation.users.UsersScreen

@Composable
fun GithubNavigation(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .semantics { testTagsAsResourceId = true }) { innerPadding ->
        NavHost(
            navController,
            startDestination = GithubScreenRoutes.UsersScreen.route,
        ) {
            composable(GithubScreenRoutes.UsersScreen.route) {
                UsersScreen(
                    modifier = Modifier.padding(innerPadding),
                    onUserClick = {
                        navController.navigate(GithubScreenRoutes.RepositoriesScreen.route)
                    },
                )
            }
            composable(GithubScreenRoutes.RepositoriesScreen.route) {
                RepositoriesScreen(
                    modifier = Modifier.padding(top = 26.dp),
                )
            }
        }
    }
}

sealed class GithubScreenRoutes(val route: String) {
    object UsersScreen : GithubScreenRoutes(route = "users_screen")
    object RepositoriesScreen : GithubScreenRoutes(route = "repositories_screen")
}
