package com.saj.simplenote.onboarding.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saj.simplenote.R
import com.saj.simplenote.domain.ui.theme.SimpleNoteTheme
import com.saj.simplenote.onboarding.ui.model.OnboardingUIModel

@Composable
fun OnboardingScreen(
    data: OnboardingUIModel,
    onGetStartedClick: () -> Unit,
) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Main illustration - centered and taking more space
            Image(
                painter = painterResource(id = R.drawable.ic_onboarding), // Using available resource
                contentDescription = null,
                modifier = Modifier.size(300.dp)
            )

            Spacer(modifier = Modifier.height(80.dp))

            // Main title text - matching the image
            Text(
                text = data.title,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 24.sp,
                ),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Left,
            )

            Spacer(modifier = Modifier.weight(1f))

            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraLarge)
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable(onClick = onGetStartedClick)
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = data.getStartedText,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onTertiary,
                    textAlign = TextAlign.Center,
                )

                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_right),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiary,
                    modifier = Modifier
                        .size(16.dp)
                        .align(Alignment.CenterEnd)
                )
            }
        }
    }
}

@Preview
@Composable
private fun OnboardingScreenPreview() {
    SimpleNoteTheme {
        OnboardingScreen(
            data = OnboardingUIModel(
                title = stringResource(R.string.onboarding_screen_title),
                welcomeText = "",
                description = "",
                getStartedText = stringResource(R.string.onboarding_get_started),
            ),
            onGetStartedClick = {},
        )
    }
}