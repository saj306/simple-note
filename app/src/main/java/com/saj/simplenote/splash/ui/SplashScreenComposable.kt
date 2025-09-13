package com.saj.simplenote.splash.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import com.saj.simplenote.domain.util.SimpleNoteRoutes
import com.saj.simplenote.splash.ui.component.splashScreen
import com.saj.simplenote.splash.ui.viewmodel.SplashScreenViewModel
import kotlinx.coroutines.delay

@Composable
fun SplashScreenComposable(
    viewModel: SplashScreenViewModel,
    navController: NavController,
) {

    // Set up navigation actions when the screen is composed
    LaunchedEffect(viewModel) {
        viewModel.navLogin.setNavigateAction {
            navController.navigate(route = SimpleNoteRoutes.LoginScreen.route) {
                popUpTo(SimpleNoteRoutes.SplashScreen.route) { inclusive = true }
            }
        }
        
        viewModel.navOnboarding.setNavigateAction {
            navController.navigate(route = SimpleNoteRoutes.OnboardingScreen.route) {
                popUpTo(SimpleNoteRoutes.SplashScreen.route) { inclusive = true }
            }
        }
        
        viewModel.navHome.setNavigateAction {
            navController.navigate(route = SimpleNoteRoutes.HomeScreen.route) {
                popUpTo(SimpleNoteRoutes.SplashScreen.route) { inclusive = true }
            }
        }
        
        // Add a delay to show the splash screen for a minimum duration
        delay(2000) // 2 seconds
        
        // Check onboarding status and navigate accordingly
        viewModel.checkOnboardingStatus()
    }

    // Display the splash screen UI
    splashScreen()
}
