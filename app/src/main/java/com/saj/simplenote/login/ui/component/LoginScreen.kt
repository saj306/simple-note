package com.saj.simplenote.login.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.saj.simplenote.R
import com.saj.simplenote.domain.ui.theme.MyGrey
import com.saj.simplenote.domain.ui.theme.MyGreyDivider
import com.saj.simplenote.domain.ui.theme.MyPurple
import com.saj.simplenote.domain.ui.theme.MyWhite
import com.saj.simplenote.domain.ui.theme.SimpleNoteTheme
import com.saj.simplenote.login.ui.model.LoginUIModel
import com.saj.simplenote.login.ui.model.LoginUIState

@Composable
fun LoginScreen(
    data: LoginUIModel,
    uiState: LoginUIState,
    onUsernameChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MyWhite)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 48.dp),
            horizontalAlignment = Alignment.Start
        ) {
            
            Spacer(modifier = Modifier.height(40.dp))

            // Title
            Text(
                text = data.title,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.onPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = data.subtitle,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSecondary,
                    fontSize = 16.sp
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Username Label
            Text(
                text = stringResource(id = R.string.username),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Username TextField
            OutlinedTextField(
                value = uiState.username,
                onValueChange = onUsernameChanged,
                placeholder = {
                    Text(
                        text = data.usernamePlaceholder,
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 16.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MyPurple,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = MyPurple
                ),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Password Label
            Text(
                text = stringResource(id = R.string.password),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Password TextField
            OutlinedTextField(
                value = uiState.password,
                onValueChange = onPasswordChanged,
                placeholder = {
                    Text(
                        text = data.passwordPlaceholder,
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 16.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MyPurple,
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = MyPurple
                ),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    Icon(
                        painter = painterResource(
                            id = if (passwordVisible) R.drawable.ic_visibility_off 
                            else R.drawable.ic_visibility
                        ),
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { passwordVisible = !passwordVisible },
                        tint = MyGrey
                    )
                }
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Login Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable(
                        onClick = onLoginClick,
                        enabled = !uiState.isLoading
                    )
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MyWhite,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = data.loginButtonText,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = "Login arrow",
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.CenterEnd),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Or text

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Left line
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = MyGreyDivider,
                    thickness = 1.dp
                )

                // Text with spacing
                Text(
                    text = stringResource(id = R.string.login_or_divider_label),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 14.sp
                   ),
                    modifier = Modifier.padding(horizontal = 16.dp),
                )

                // Right line
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    color = MyGreyDivider,
                    thickness = 1.dp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Register link
            Text(
                text = data.registerLinkText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onRegisterClick() },
                textAlign = TextAlign.Center
            )

            // Error message
            uiState.errorMessage?.let { error ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    SimpleNoteTheme {
        LoginScreen(
            data = LoginUIModel(
                title = "Let's Login",
                subtitle = "And notes your idea",
                usernamePlaceholder = "saj306",
                passwordPlaceholder = "********",
                loginButtonText = "Login",
                registerLinkText = "Don't have any account? Register here"
            ),
            uiState = LoginUIState(),
            onUsernameChanged = {},
            onPasswordChanged = {},
            onLoginClick = {},
            onRegisterClick = {}
        )
    }
}
