package com.saj.simplenote.register.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.saj.simplenote.R
import com.saj.simplenote.domain.util.SimpleNoteRoutes
import com.saj.simplenote.register.ui.component.RegisterScreen
import com.saj.simplenote.register.ui.model.RegisterUIModel
import com.saj.simplenote.register.ui.viewmodel.RegisterViewModel

@Composable
fun RegisterComposable(
    viewModel: RegisterViewModel,
    navController: NavController,
    finish: () -> Unit,
) {
    
    val uiState by viewModel.uiState.collectAsState()

    // Set up navigation actions when the screen is composed
    LaunchedEffect(Unit) {
        viewModel.navLogin.setNavigateAction {
            navController.navigate(route = SimpleNoteRoutes.LoginScreen.route) {
                popUpTo(SimpleNoteRoutes.RegisterScreen.route) { inclusive = true }
            }
        }
        viewModel.navBack.setNavigateAction {
            navController.popBackStack()
        }
    }

    // Create the UI model with string resources
    val registerData = RegisterUIModel(
        title = stringResource(id = R.string.register_title),
        subtitle = stringResource(id = R.string.register_subtitle),
        firstNamePlaceholder = stringResource(id = R.string.register_first_name_placeholder),
        lastNamePlaceholder = stringResource(id = R.string.register_last_name_placeholder),
        usernamePlaceholder = stringResource(id = R.string.register_username_placeholder),
        emailPlaceholder = stringResource(id = R.string.register_email_placeholder),
        passwordPlaceholder = stringResource(id = R.string.register_password_placeholder),
        retypePasswordPlaceholder = stringResource(id = R.string.register_retype_password_placeholder),
        registerButtonText = stringResource(id = R.string.register_button_text),
        loginLinkText = stringResource(id = R.string.register_login_link),
    )

    RegisterScreen(
        data = registerData,
        uiState = uiState,
        onFirstNameChanged = viewModel::onFirstNameChanged,
        onLastNameChanged = viewModel::onLastNameChanged,
        onUsernameChanged = viewModel::onUsernameChanged,
        onEmailChanged = viewModel::onEmailChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onRetypePasswordChanged = viewModel::onRetypePasswordChanged,
        onRegisterClick = viewModel::onRegisterClick,
        onLoginClick = viewModel::onLoginClick
    )

    BackHandler {
        viewModel.navBack.navigate()
    }
}
