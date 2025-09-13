package com.saj.simplenote.settings.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.saj.simplenote.R
import com.saj.simplenote.domain.util.SimpleNoteRoutes
import com.saj.simplenote.settings.ui.component.SettingsScreen
import com.saj.simplenote.settings.ui.model.SettingsUIModel
import com.saj.simplenote.settings.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsComposable(
    viewModel: SettingsViewModel,
    navController: NavController,
    finish: () -> Unit,
) {
    
    val uiState by viewModel.uiState.collectAsState()

    // Set up navigation actions when the screen is composed
    LaunchedEffect(Unit) {
        viewModel.navBack.setNavigateAction {
            navController.popBackStack()
        }
        
        viewModel.navLogin.setNavigateAction {
            navController.navigate(route = SimpleNoteRoutes.LoginScreen.route) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
        
        viewModel.navChangePassword.setNavigateAction {
            println("SettingsComposable: Navigating to Change Password")
            navController.navigate(route = SimpleNoteRoutes.ChangePasswordScreen.route) {
                launchSingleTop = true
            }
        }
    }

    // Create the UI model with string resources
    val settingsData = SettingsUIModel(
        title = stringResource(id = R.string.settings_screen_title),
        changePasswordTitle = stringResource(id = R.string.settings_change_password_title),
        logOutTitle = stringResource(id = R.string.settings_logout_button_label),
        logoutDialogTitle = stringResource(id = R.string.settings_logout_dialog_title),
        logoutDialogMessage = stringResource(id = R.string.settings_logout_dialog_message),
        logoutDialogCancel = stringResource(id = R.string.settings_logout_dialog_cancel),
        logoutDialogConfirm = stringResource(id = R.string.settings_logout_dialog_confirm),
    )

    SettingsScreen(
        data = settingsData,
        uiState = uiState,
        onBackClick = { viewModel.onBackClick() },
        onChangePasswordClick = { viewModel.onChangePasswordClick() },
        onLogOutClick = { viewModel.onLogOutClick() },
        onLogoutDialogCancel = { viewModel.onLogoutDialogCancel() },
        onLogoutDialogConfirm = { viewModel.onLogoutDialogConfirm() }
    )

    BackHandler {
        viewModel.onBackClick()
    }
}
