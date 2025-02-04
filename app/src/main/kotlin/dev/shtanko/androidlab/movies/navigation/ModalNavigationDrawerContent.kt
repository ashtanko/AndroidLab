package dev.shtanko.androidlab.movies.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
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
fun ModalNavigationDrawerContent(
    currentDestination: NavDestination?,
    navigationContentPosition: MoviesNavigationContentPosition,
    modifier: Modifier = Modifier,
    navigateToTopLevelDestination: (MoviesTopLevelDestination) -> Unit = {},
    onDrawerClicked: () -> Unit = {},
) {
    ModalDrawerSheet {
        // TODO remove custom nav drawer content positioning when NavDrawer component supports it. ticket : b/232495216
        Layout(
            modifier = modifier
                .background(MaterialTheme.colorScheme.inverseOnSurface)
                .padding(16.dp),
            content = {
                Column(
                    modifier = Modifier.layoutId(LayoutType.HEADER),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = stringResource(id = R.string.app_name).uppercase(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                        IconButton(onClick = onDrawerClicked) {
                            Icon(
                                imageVector = Icons.Outlined.Android,
                                contentDescription = null,
                            )
                        }
                    }
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
            measurePolicy = navigationMeasurePolicy(
                navigationContentPosition,
            ),
        )
    }
}

@ThemePreviews
@Composable
private fun ModalNavigationDrawerContentTopPreview() {
    AndroidLabTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        ModalNavigationDrawerContent(
            currentDestination = currentDestination,
            navigationContentPosition = MoviesNavigationContentPosition.TOP,
        )
    }
}

@ThemePreviews
@Composable
private fun ModalNavigationDrawerContentCenterPreview() {
    AndroidLabTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        ModalNavigationDrawerContent(
            currentDestination = currentDestination,
            navigationContentPosition = MoviesNavigationContentPosition.CENTER,
        )
    }
}
