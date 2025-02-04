package dev.shtanko.androidlab.movies.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import dev.shtanko.androidlab.utils.ThemePreviews

@Composable
fun MoviesNavigationRail(
    currentDestination: NavDestination?,
    modifier: Modifier = Modifier,
    navigateToTopLevelDestination: (MoviesTopLevelDestination) -> Unit = {},
    onDrawerClicked: () -> Unit = {},
) {
    NavigationRail(
        modifier = modifier.fillMaxHeight(),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface,
    ) {
        Column(
            modifier = Modifier.layoutId(LayoutType.HEADER),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            NavigationRailItem(
                selected = false,
                onClick = onDrawerClicked,
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Android,
                        contentDescription = null,
                    )
                },
            )
            Spacer(Modifier.height(8.dp)) // NavigationRailHeaderPadding
            Spacer(Modifier.height(4.dp)) // NavigationRailVerticalPadding
        }

        Column(
            modifier = Modifier.layoutId(LayoutType.CONTENT),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            TOP_LEVEL_DESTINATIONS.forEach { moviesDestination ->
                NavigationRailItem(
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
                                contentDescription = stringResource(
                                    id = moviesDestination.iconTextId,
                                ),
                            )
                        } else {
                            Icon(
                                imageVector = moviesDestination.unselectedIcon,
                                contentDescription = stringResource(
                                    id = moviesDestination.iconTextId,
                                ),
                            )
                        }
                    },
                )
            }
        }
    }
}

@ThemePreviews
@Composable
private fun MoviesNavigationRailTopPreview() {
    AndroidLabTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        MoviesNavigationRail(
            currentDestination = currentDestination,
        )
    }
}
