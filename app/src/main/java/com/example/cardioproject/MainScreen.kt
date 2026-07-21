package com.example.cardioproject

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.EventNote
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import com.example.cardioproject.anthropometric.presentation.screen.AddAnthropometricScreen
import com.example.cardioproject.core.navigation.Route
import com.example.cardioproject.core.navigation.TopLevelBackStack
import com.example.cardioproject.home.presentation.HeartRateMainScreen
import com.example.cardioproject.workout.domain.model.WorkoutSettings
import com.example.cardioproject.workout.presentation.screen.WorkoutScreen
import com.example.cardioproject.workout.presentation.screen.WorkoutSettingsScreen
import com.example.cardioproject.workout.presentation.screen.WorkoutSettingsScreenContent
import com.example.cardioproject.workout.presentation.viewmodel.WorkoutSettingsViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.java.KoinJavaComponent.inject
import kotlin.collections.listOf

interface TopLevelRoute: Route {
    val icon: ImageVector
}

data object Main: TopLevelRoute {
    override val icon: ImageVector = Icons.Outlined.Home
}

data object WorkoutHistory: TopLevelRoute {
    override val icon: ImageVector = Icons.Outlined.History
}

data object Anthropometric: TopLevelRoute {
    override val icon: ImageVector = Icons.AutoMirrored.Outlined.EventNote
}

data object AddAnthropometric: Route
data object WorkoutSetting: Route
data class ActiveWorkout(val settings: WorkoutSettings): Route

@Composable
fun MainScreen() {
    val topLevelBackStack by inject<TopLevelBackStack<Route>>(TopLevelBackStack::class.java)
    val currentRoute = topLevelBackStack.topLevelKey

    Scaffold(
        bottomBar = {
            NavigationBar {
                val allTabs = listOf(Main, WorkoutHistory, Anthropometric)

                val visibleTabs = allTabs.filter {
                    route -> !(route == Main && currentRoute == Main)
                }
                visibleTabs.forEach { route ->
                    NavigationBarItem(
                        icon = { Icon(route.icon, null) },
                        selected = currentRoute == route,
                        onClick = { topLevelBackStack.addTopLevel(route) }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavDisplay(
            backStack = topLevelBackStack.backStack,
            onBack = { topLevelBackStack.removeLast() },
            modifier = Modifier.padding(paddingValues),
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            sceneStrategies = listOf(DialogSceneStrategy()),
            entryProvider = entryProvider {
                entry<Main> {
                    HeartRateMainScreen(
                        onAddMeasurementsClick = { topLevelBackStack.add(AddAnthropometric) },
                        onStartWorkoutClick = { topLevelBackStack.add(WorkoutSetting) }
                    )
                }
                entry<WorkoutHistory> {

                }
                entry<Anthropometric> {

                }
                entry<AddAnthropometric> {
                    AddAnthropometricScreen(onBackClick = { topLevelBackStack.removeLast() })
                }

                entry<WorkoutSetting>(
                    metadata = DialogSceneStrategy.dialog(
                        DialogProperties(usePlatformDefaultWidth = false)
                    )
                ) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        WorkoutSettingsScreen(
                            onNavigateToActiveWorkout = { settings ->
                                topLevelBackStack.removeLast()
                                topLevelBackStack.add(ActiveWorkout(settings))
                            },
                            onBackClick = { topLevelBackStack.removeLast() },
                        )
                    }
                }

                entry<ActiveWorkout> { activeWorkout ->
                    WorkoutScreen(
                        settings = activeWorkout.settings,
                        onFinish = {
                            topLevelBackStack.removeLast()
                        }
                    )
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}
