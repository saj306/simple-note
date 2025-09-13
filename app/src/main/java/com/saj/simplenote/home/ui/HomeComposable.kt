package com.saj.simplenote.home.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.saj.simplenote.R
import com.saj.simplenote.domain.util.SimpleNoteRoutes
import com.saj.simplenote.home.ui.component.HomeScreen
import com.saj.simplenote.home.ui.model.HomeUIModel
import com.saj.simplenote.home.ui.viewmodel.HomeViewModel

@Composable
fun HomeComposable(
    viewModel: HomeViewModel,
    navController: NavController,
    finish: () -> Unit,
) {
    
    val uiState by viewModel.uiState.collectAsState()

    // Set up navigation actions when the screen is composed
    LaunchedEffect(Unit) {
        viewModel.navSettings.setNavigateAction {
            navController.navigate(route = SimpleNoteRoutes.SettingsScreen.route) {
                launchSingleTop = true
            }
        }
        
        viewModel.navNote.setNavigateAction { noteId ->
            val route = if (noteId != null) {
                "${SimpleNoteRoutes.NoteScreen.route}?noteId=$noteId"
            } else {
                SimpleNoteRoutes.NoteScreen.route
            }
            navController.navigate(route = route) {
                launchSingleTop = true
            }
        }
        
        viewModel.navLogin.setNavigateAction {
            navController.navigate(route = SimpleNoteRoutes.LoginScreen.route) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    // Create the UI model with string resources
    val homeData = HomeUIModel(
        title = stringResource(id = R.string.home_title),
        subtitle = stringResource(id = R.string.home_subtitle),
        homeBottomNav = stringResource(id = R.string.home_bottom_nav_home),
        settingsBottomNav = stringResource(id = R.string.home_bottom_nav_settings),
        addNoteButton = stringResource(id = R.string.home_add_note_button),
    )

    HomeScreen(
        data = homeData,
        uiState = uiState,
        onAddNoteClick = { viewModel.onAddNoteClick() },
        onSettingsClick = { viewModel.onSettingsClick() },
        onHomeClick = { /* Already on home screen */ },
        onNoteClick = { noteId -> viewModel.onNoteClick(noteId) },
        onSearchQueryChanged = { query -> viewModel.onSearchQueryChanged(query) },
        onLoadMore = { viewModel.loadMoreNotes() },
        onRefresh = { viewModel.refreshNotes() }
    )

    BackHandler {
        finish()
    }
}
