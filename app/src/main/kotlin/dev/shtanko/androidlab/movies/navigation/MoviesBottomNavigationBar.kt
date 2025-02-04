package dev.shtanko.androidlab.movies.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import dev.shtanko.androidlab.utils.ThemePreviews

@Composable
fun MoviesBottomNavigationBar(
    currentDestination: NavDestination?,
    navigateToTopLevelDestination: (MoviesTopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    NavigationBar(modifier = modifier.fillMaxWidth()) {
        TOP_LEVEL_DESTINATIONS.forEach { moviesDestination ->
            NavigationBarItem(
                selected = currentDestination?.route == moviesDestination.route.path,
                onClick = { navigateToTopLevelDestination(moviesDestination) },
                label = {
                    Text(
                        text = stringResource(id = moviesDestination.iconTextId),
                        color = getLabelColor(currentDestination?.route == moviesDestination.route.path),
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                },
                icon = {
                    if (currentDestination?.route == moviesDestination.route.path) {
                        Icon(
                            imageVector = moviesDestination.selectedIcon,
                            contentDescription = stringResource(id = moviesDestination.iconTextId),
                        )
                    } else {
                        Icon(
                            imageVector = moviesDestination.unselectedIcon,
                            contentDescription = stringResource(id = moviesDestination.iconTextId),
                        )
                    }
                },
            )
        }
    }
}

@ThemePreviews
@Composable
private fun MoviesBottomNavigationBarPreview() {
    AndroidLabTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        MoviesBottomNavigationBar(
            currentDestination = currentDestination,
            navigateToTopLevelDestination = { },
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
