package com.saj.simplenote.changepassword.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.saj.simplenote.R
import com.saj.simplenote.changepassword.ui.component.ChangePasswordScreen
import com.saj.simplenote.changepassword.ui.model.ChangePasswordUIModel
import com.saj.simplenote.changepassword.ui.viewmodel.ChangePasswordViewModel

@Composable
fun ChangePasswordComposable(
    viewModel: ChangePasswordViewModel,
    navController: NavController,
    finish: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()

    // Set up navigation actions when the screen is composed
    LaunchedEffect(Unit) {
        viewModel.navBack.setNavigateAction {
            navController.popBackStack()
        }
    }

    // Create the UI model with string resources
    val changePasswordData = ChangePasswordUIModel(
        title = stringResource(id = R.string.change_password_screen_title),
        currentPasswordLabel = stringResource(id = R.string.change_password_current_password),
        newPasswordLabel = stringResource(id = R.string.change_password_new_password),
        retypeNewPasswordLabel = stringResource(id = R.string.change_password_retype_new_password),
        currentPasswordPlaceholder = stringResource(id = R.string.change_password_current_password_placeholder),
        newPasswordPlaceholder = stringResource(id = R.string.change_password_new_password_placeholder),
        retypeNewPasswordPlaceholder = stringResource(id = R.string.change_password_retype_new_password_placeholder),
        submitButtonText = stringResource(id = R.string.change_password_submit_button),
        backButtonText = stringResource(id = R.string.change_password_back_button)
    )

    ChangePasswordScreen(
        data = changePasswordData,
        uiState = uiState,
        onCurrentPasswordChanged = viewModel::onCurrentPasswordChanged,
        onNewPasswordChanged = viewModel::onNewPasswordChanged,
        onRetypeNewPasswordChanged = viewModel::onRetypeNewPasswordChanged,
        onSubmitClick = viewModel::onSubmitClick,
        onBackClick = viewModel::onBackClick
    )

    BackHandler {
        viewModel.onBackClick()
    }
}
