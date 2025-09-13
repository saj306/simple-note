package com.saj.simplenote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.saj.simplenote.changepassword.ui.ChangePasswordComposable
import com.saj.simplenote.changepassword.ui.viewmodel.ChangePasswordViewModel
import com.saj.simplenote.domain.ui.theme.SimpleNoteTheme
import com.saj.simplenote.domain.util.SimpleNoteRoutes
import com.saj.simplenote.home.ui.HomeComposable
import com.saj.simplenote.home.ui.viewmodel.HomeViewModel
import com.saj.simplenote.login.ui.LoginComposable
import com.saj.simplenote.login.ui.viewmodel.LoginViewModel
import com.saj.simplenote.note.ui.NoteComposable
import com.saj.simplenote.note.ui.viewmodel.NoteViewModel
import com.saj.simplenote.onboarding.ui.OnboardingComposable
import com.saj.simplenote.onboarding.ui.viewmodel.OnboardingViewModel
import com.saj.simplenote.register.ui.RegisterComposable
import com.saj.simplenote.register.ui.viewmodel.RegisterViewModel
import com.saj.simplenote.settings.ui.SettingsComposable
import com.saj.simplenote.settings.ui.viewmodel.SettingsViewModel
import com.saj.simplenote.splash.ui.SplashScreenComposable
import com.saj.simplenote.splash.ui.viewmodel.SplashScreenViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SimpleNoteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SimpleNoteApp(
                        onExitApp = { finish() }
                    )
                }
            }
        }
    }
}

@Composable
fun SimpleNoteApp(
    onExitApp: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SimpleNoteRoutes.SplashScreen.route,
        enterTransition = { fadeIn(animationSpec = tween(700)) },
        exitTransition = { fadeOut(animationSpec = tween(700)) },
        popEnterTransition = { fadeIn(animationSpec = tween(700)) },
        popExitTransition = { fadeOut(animationSpec = tween(700)) },
    ) {

        composable(route = SimpleNoteRoutes.SplashScreen.route) {
            val viewModel: SplashScreenViewModel = koinViewModel()
            SplashScreenComposable(
                viewModel = viewModel,
                navController = navController
            )
        }

        composable(route = SimpleNoteRoutes.OnboardingScreen.route) {
            val viewModel: OnboardingViewModel = koinViewModel()
            OnboardingComposable(
                viewModel = viewModel,
                navController = navController,
                finish = onExitApp
            )
        }

        composable(route = SimpleNoteRoutes.LoginScreen.route) {
            val viewModel: LoginViewModel = koinViewModel()
            LoginComposable(
                viewModel = viewModel,
                navController = navController,
                finish = onExitApp
            )
        }

        composable(route = SimpleNoteRoutes.RegisterScreen.route) {
            val viewModel: RegisterViewModel = koinViewModel()
            RegisterComposable(
                viewModel = viewModel,
                navController = navController,
                finish = onExitApp
            )
        }

        composable(route = SimpleNoteRoutes.HomeScreen.route) {
            val viewModel: HomeViewModel = koinViewModel()
            HomeComposable(
                viewModel = viewModel,
                navController = navController,
                finish = onExitApp
            )
        }

        composable(
            route = "${SimpleNoteRoutes.NoteScreen.route}?noteId={noteId}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val viewModel: NoteViewModel = koinViewModel()
            val noteIdString = backStackEntry.arguments?.getString("noteId")
            val noteId = noteIdString?.toIntOrNull()

            NoteComposable(
                viewModel = viewModel,
                navController = navController,
                noteId = noteId,
                finish = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = SimpleNoteRoutes.SettingsScreen.route) {
            val viewModel: SettingsViewModel = koinViewModel()
            SettingsComposable(
                viewModel = viewModel,
                navController = navController,
                finish = onExitApp
            )
        }

        composable(route = SimpleNoteRoutes.ChangePasswordScreen.route) {
            val viewModel: ChangePasswordViewModel = koinViewModel()
            ChangePasswordComposable(
                viewModel = viewModel,
                navController = navController,
                finish = onExitApp
            )
        }
    }
}