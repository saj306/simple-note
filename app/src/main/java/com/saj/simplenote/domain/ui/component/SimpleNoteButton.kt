package com.saj.simplenote.domain.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.saj.simplenote.domain.util.SimpleNoteRoutes
import com.saj.simplenote.onboarding.ui.model.OnboardingUIModel

@Composable
fun SimpleNoteButton(
    data: OnboardingUIModel,
    onGetStartedClick: () -> Unit,
) {

    Button(
        onClick = onGetStartedClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        Text(
            text = data.getStartedText,
            style = MaterialTheme.typography.bodyLarge
        )

    }

}

@Preview
@Composable
fun SimpleNoteButtonPreview() {
    SimpleNoteButton(
        data = OnboardingUIModel(
            title = "Welcome to Simple Note",
            welcomeText = "Welcome to Simple Note",
            description = "This is a simple note-taking app.",
            getStartedText = "Get Started"
        ),
        onGetStartedClick = { /* Do nothing */ }
    )
}