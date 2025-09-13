package com.saj.simplenote.login.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.saj.simplenote.R
import com.saj.simplenote.domain.util.SimpleNoteRoutes
import com.saj.simplenote.login.ui.component.LoginScreen
import com.saj.simplenote.login.ui.model.LoginUIModel
import com.saj.simplenote.login.ui.viewmodel.LoginViewModel

@Composable
fun LoginComposable(
    viewModel: LoginViewModel,
    navController: NavController,
    finish: () -> Unit,
) {
    
    val uiState by viewModel.uiState.collectAsState()

    // Set up navigation actions when the screen is composed
    LaunchedEffect(Unit) {
        viewModel.navMain.setNavigateAction {
            navController.navigate(route = SimpleNoteRoutes.HomeScreen.route) {
                popUpTo(SimpleNoteRoutes.LoginScreen.route) { inclusive = true }
            }
        }
        
        viewModel.navRegister.setNavigateAction {
            navController.navigate(route = SimpleNoteRoutes.RegisterScreen.route)
        }
    }

    // Create the UI model with string resources
    val loginData = LoginUIModel(
        title = stringResource(id = R.string.login_title),
        subtitle = stringResource(id = R.string.login_subtitle),
        usernamePlaceholder = stringResource(id = R.string.login_username_placeholder),
        passwordPlaceholder = stringResource(id = R.string.login_password_placeholder),
        loginButtonText = stringResource(id = R.string.login_button_label),
        registerLinkText = stringResource(id = R.string.login_register_link)
    )

    LoginScreen(
        data = loginData,
        uiState = uiState,
        onUsernameChanged = viewModel::onUsernameChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onLoginClick = viewModel::onLoginClick,
        onRegisterClick = viewModel::onRegisterClick
    )

    BackHandler {
        finish()
    }
}
