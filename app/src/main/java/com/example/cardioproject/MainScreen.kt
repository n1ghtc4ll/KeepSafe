package com.example.cardioproject

import android.Manifest
import androidx.annotation.RequiresPermission
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
import com.example.cardioproject.anthrodiary.presentation.AnthroDiaryRoute
import com.example.cardioproject.core.navigation.Route
import com.example.cardioproject.core.navigation.TopLevelBackStack
import com.example.cardioproject.presentation.HeartRateMainScreen
import com.example.cardioproject.workout.presentation.screen.WorkoutDetailsScreen
import com.example.cardioproject.workout.presentation.screen.WorkoutHistoryScreen
import com.example.cardioproject.settings.presentation.screen.SettingsRoute
import com.example.cardioproject.settings.presentation.screen.ProfileBuilderRoute
import com.example.cardioproject.workout.domain.model.WorkoutSettings
import com.example.cardioproject.workout.presentation.screen.WorkoutScreen
import com.example.cardioproject.workout.presentation.screen.WorkoutSettingsScreen
import com.example.cardioproject.workout.presentation.viewmodel.WorkoutDetailsViewModel
import com.example.cardioproject.workout.presentation.viewmodel.WorkoutHistoryViewModel
import org.koin.androidx.compose.koinViewModel
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
data object Settings: Route
data object DeviceSearch: Route
data class ProfileBuilder(val profileId: String? = null): Route
data class ActiveWorkout(val settings: WorkoutSettings): Route
data class WorkoutDetails(val workoutId: String) : Route

@RequiresPermission(allOf = [
    Manifest.permission.BLUETOOTH_SCAN,
    Manifest.permission.BLUETOOTH_CONNECT
])
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
                        onStartWorkoutClick = { topLevelBackStack.add(WorkoutSetting) },
                        onSettingsClick = { topLevelBackStack.add(Settings) }
                    )
                }
                entry<WorkoutHistory> {
                    val viewModel: WorkoutHistoryViewModel = koinViewModel()
                    val workouts by viewModel.workouts.collectAsState()

                    WorkoutHistoryScreen(
                        workouts = workouts,
                        onBackClick = { topLevelBackStack.removeLast() },
                        onWorkoutClick = { id ->
                            topLevelBackStack.add(WorkoutDetails(id))
                        },
                        onAddWorkout = { },
                        onEditWorkout = {  },
                        onDeleteWorkout = { session ->
                            // viewModel.deleteWorkoutById(session.id)
                        }
                    )
                }

                entry<WorkoutDetails> { route ->
                    val viewModel: WorkoutDetailsViewModel = koinViewModel(
                        parameters = { org.koin.core.parameter.parametersOf(route.workoutId) }
                    )
                    val workout by viewModel.workout.collectAsState()

                    workout?.let { loadedWorkout ->
                        WorkoutDetailsScreen(
                            workout = loadedWorkout,
                            onBackClick = { topLevelBackStack.removeLast() },
                            onWorkoutUpdated = { updatedWorkout ->
                                viewModel.updateTag(updatedWorkout.tag)
                            }
                        )
                    }
                }
                entry<Anthropometric> {
                    AnthroDiaryRoute(onBackClick = { topLevelBackStack.removeLast() })
                }
                entry<AddAnthropometric> {
                    AnthroDiaryRoute(onBackClick = { topLevelBackStack.removeLast() })
                }
                entry<Settings> {
                    SettingsRoute(
                        onBackClick = { topLevelBackStack.removeLast() },
                        onNavigateToProfileBuilder = { profileId ->
                            topLevelBackStack.add(ProfileBuilder(profileId))
                        }
                    )
                }

                /*entry<DeviceSearch> {
                    DeviceScanBottomSheet(onDismiss = { topLevelBackStack.removeLast() })
                }*/
                entry<ProfileBuilder> { route ->
                    ProfileBuilderRoute(
                        profileId = route.profileId,
                        onNavigateBack = { topLevelBackStack.removeLast() }
                    )
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
                            topLevelBackStack.addTopLevel(WorkoutHistory)
                        }
                    )
                }
            }
        )
    }
}

@RequiresPermission(allOf = [Manifest.permission.BLUETOOTH_SCAN, Manifest.permission.BLUETOOTH_CONNECT])
@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    MainScreen()
}
