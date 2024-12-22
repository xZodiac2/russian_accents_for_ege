package com.ilya.russianaccents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ilya.home.screen.HomeScreen
import com.ilya.mistakereview.screen.MistakesReviewScreen
import com.ilya.russianaccents.navigation.BottomBarItem
import com.ilya.russianaccents.navigation.Screen
import com.ilya.training.screen.TrainingScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val splashScreenViewModel by viewModels<SplashScreenViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply { setKeepOnScreenCondition { splashScreenViewModel.showSplashScreen } }
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val currentBackStackEntry = navController.currentBackStackEntryAsState()

            Scaffold(
                containerColor = Color.White,
                bottomBar = {
                    val isBottomBarVisible = isBottomBarVisible(currentBackStackEntry.value)
                    if (isBottomBarVisible) {
                        BottomBar(navController)
                    }
                }
            ) { padding ->
                Navigation(
                    padding = padding,
                    navController = navController
                )
            }
        }
    }

    @NonRestartableComposable
    @Composable
    private fun isBottomBarVisible(backStackEntry: NavBackStackEntry?): Boolean {
        return backStackEntry?.destination?.hasRoute(Screen.Training::class) == false
    }

    @Composable
    private fun Navigation(
        padding: PaddingValues,
        navController: NavHostController,
    ) {
        NavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
            startDestination = Screen.Home
        ) {
            composable<Screen.Home>(
                enterTransition = { fadeIn(tween(0)) },
                exitTransition = { fadeOut(tween(0)) }
            ) {
                HomeScreen { navController.navigate(Screen.Training(false)) }
            }
            composable<Screen.Training>(
                enterTransition = { fadeIn(tween(0)) },
                exitTransition = { fadeOut(tween(0)) }
            ) {
                val route = it.toRoute<Screen.Training>()
                TrainingScreen(route.mistakesOnly) { navController.popBackStack() }
            }
            composable<Screen.MistakeReview>(
                enterTransition = { fadeIn(tween(0)) },
                exitTransition = { fadeOut(tween(0)) }
            ) {
                MistakesReviewScreen { navController.navigate(Screen.Training(true)) }
            }
        }
    }

    @Composable
    private fun BottomBar(navController: NavController) {
        val backStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = backStackEntry.value?.destination
        Column {
            HorizontalDivider()
            NavigationBar(containerColor = Color.White) {
                for (item in listOf(BottomBarItem.Home, BottomBarItem.MistakeReview)) {
                    NavigationBarItem(
                        selected = currentDestination?.hierarchy?.any { it.hasRoute(item.screen::class) } == true,
                        onClick = {
                            navController.navigate(item.screen) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                restoreState = true
                                launchSingleTop = true
                            }
                        },
                        label = { Text(item.label) },
                        icon = {
                            Icon(
                                modifier = Modifier.size(30.dp),
                                imageVector = ImageVector.vectorResource(item.iconId),
                                contentDescription = "bottomBarIcon"
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color(221, 241, 255, 255)
                        )
                    )
                }
            }
        }
    }

}

