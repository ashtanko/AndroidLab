package dev.shtanko.androidlab.movies.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.shtanko.androidlab.R
import dev.shtanko.androidlab.ui.theme.AndroidLabTheme
import dev.shtanko.androidlab.utils.MoviesNavigationContentPosition
import dev.shtanko.androidlab.utils.ThemePreviews

@Composable
fun PermanentNavigationDrawerContent(
    currentDestination: NavDestination?,
    navigationContentPosition: MoviesNavigationContentPosition,
    modifier: Modifier = Modifier,
    navigateToTopLevelDestination: (MoviesTopLevelDestination) -> Unit = {},
) {
    PermanentDrawerSheet(
        modifier = modifier.sizeIn(minWidth = 200.dp, maxWidth = 300.dp),
        drawerContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {
        // TODO remove custom nav drawer content positioning when NavDrawer component supports it. ticket : b/232495216
        Layout(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .padding(16.dp),
            content = {
                Column(
                    modifier = Modifier.layoutId(LayoutType.HEADER),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        modifier = Modifier
                            .padding(16.dp),
                        text = stringResource(id = R.string.app_name).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                Column(
                    modifier = Modifier
                        .layoutId(LayoutType.CONTENT)
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    TOP_LEVEL_DESTINATIONS.forEach { moviesDestination ->
                        NavigationDrawerItem(
                            selected = currentDestination?.route == moviesDestination.route.path,
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
                            colors = NavigationDrawerItemDefaults.colors(
                                unselectedContainerColor = Color.Transparent,
                            ),
                            onClick = { navigateToTopLevelDestination(moviesDestination) },
                        )
                    }
                }
            },
            measurePolicy = navigationMeasurePolicy(navigationContentPosition),
        )
    }
}

@ThemePreviews
@Composable
private fun PermanentNavigationDrawerContentTopPreview() {
    AndroidLabTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        PermanentNavigationDrawerContent(
            currentDestination = currentDestination,
            navigationContentPosition = MoviesNavigationContentPosition.TOP,
        )
    }
}

@ThemePreviews
@Composable
private fun PermanentNavigationDrawerContentCenterPreview() {
    AndroidLabTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        PermanentNavigationDrawerContent(
            currentDestination = currentDestination,
            navigationContentPosition = MoviesNavigationContentPosition.CENTER,
        )
    }
}
