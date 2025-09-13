package com.saj.simplenote.onboarding.ui

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.saj.simplenote.R
import com.saj.simplenote.domain.util.SimpleNoteRoutes
import com.saj.simplenote.onboarding.ui.component.OnboardingScreen
import com.saj.simplenote.onboarding.ui.model.OnboardingUIModel
import com.saj.simplenote.onboarding.ui.viewmodel.OnboardingViewModel

@Composable
fun OnboardingComposable(
    viewModel: OnboardingViewModel,
    navController: NavController,
    finish: () -> Unit,
) {

    // Set up navigation action when the screen is composed
    LaunchedEffect(Unit) {
        viewModel.navLogin.setNavigateAction {
            navController.navigate(route = SimpleNoteRoutes.LoginScreen.route) {
                popUpTo(SimpleNoteRoutes.OnboardingScreen.route) { inclusive = true }
            }
        }
    }

    // Create the UI model with string resources
    val onboardingData = OnboardingUIModel(
        title = stringResource(id = R.string.onboarding_screen_title),
        welcomeText = "", // Not used in the current design
        description = "", // Not used in the current design
        getStartedText = stringResource(id = R.string.onboarding_get_started),
    )

    OnboardingScreen(
        data = onboardingData,
        onGetStartedClick = {
            viewModel.onGetStartedClick()
        },
    )

    BackHandler {
        finish()
    }
}