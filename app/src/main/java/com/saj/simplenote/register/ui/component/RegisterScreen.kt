package com.saj.simplenote.register.ui.component

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import com.saj.simplenote.domain.ui.theme.MyPurple
import com.saj.simplenote.domain.ui.theme.MyWhite
import com.saj.simplenote.domain.ui.theme.SimpleNoteTheme
import com.saj.simplenote.register.ui.model.RegisterUIModel
import com.saj.simplenote.register.ui.model.RegisterUIState

@Composable
fun RegisterScreen(
    data: RegisterUIModel,
    uiState: RegisterUIState,
    onFirstNameChanged: (String) -> Unit,
    onLastNameChanged: (String) -> Unit,
    onUsernameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onRetypePasswordChanged: (String) -> Unit,
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var retypePasswordVisible by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

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
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.Start
        ) {
            
            // Back to Login
            Row(
                modifier = Modifier
                    .clickable { onLoginClick() }
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_simple_arrow_back),
                    contentDescription = "Back to Login",
                    modifier = Modifier.size(10.dp),
                    tint = MyPurple
                )
                Spacer(modifier = Modifier.padding(4.dp))
                Text(
                    text = stringResource(id = R.string.register_back_to_login),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MyPurple,
                        fontSize = 16.sp
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))

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

            Spacer(modifier = Modifier.height(32.dp))

            // First Name
            Text(
                text = stringResource(id = R.string.register_first_name),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.firstName,
                onValueChange = onFirstNameChanged,
                placeholder = {
                    Text(
                        text = data.firstNamePlaceholder,
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

            Spacer(modifier = Modifier.height(20.dp))

            // Last Name
            Text(
                text = stringResource(id = R.string.register_last_name),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.lastName,
                onValueChange = onLastNameChanged,
                placeholder = {
                    Text(
                        text = data.lastNamePlaceholder,
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

            Spacer(modifier = Modifier.height(20.dp))

            // Username
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

            Spacer(modifier = Modifier.height(20.dp))

            // Email Address
            Text(
                text = stringResource(id = R.string.register_email),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.email,
                onValueChange = onEmailChanged,
                placeholder = {
                    Text(
                        text = data.emailPlaceholder,
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Password
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

            Spacer(modifier = Modifier.height(20.dp))

            // Retype Password
            Text(
                text = stringResource(id = R.string.register_retype_password),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontSize = 16.sp
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.retypePassword,
                onValueChange = onRetypePasswordChanged,
                placeholder = {
                    Text(
                        text = data.retypePasswordPlaceholder,
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
                visualTransformation = if (retypePasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    Icon(
                        painter = painterResource(
                            id = if (retypePasswordVisible) R.drawable.ic_visibility_off 
                            else R.drawable.ic_visibility
                        ),
                        contentDescription = if (retypePasswordVisible) "Hide password" else "Show password",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { retypePasswordVisible = !retypePasswordVisible },
                        tint = MyGrey
                    )
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Register Button
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(100.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .clickable(
                        onClick = onRegisterClick,
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
                        text = data.registerButtonText,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        textAlign = TextAlign.Center
                    )

                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_right),
                        contentDescription = "Register arrow",
                        modifier = Modifier
                            .size(16.dp)
                            .align(Alignment.CenterEnd),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login link
            Text(
                text = data.loginLinkText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onTertiary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLoginClick() },
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
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    SimpleNoteTheme {
        RegisterScreen(
            data = RegisterUIModel(
                title = "Register",
                subtitle = "And start taking notes",
                firstNamePlaceholder = "Example: Ali",
                lastNamePlaceholder = "Example: Jafari",
                usernamePlaceholder = "Example: @AliJafari",
                emailPlaceholder = "Example: jafari.ali@gmail.com",
                passwordPlaceholder = "********",
                retypePasswordPlaceholder = "********",
                registerButtonText = "Register",
                loginLinkText = "Already have an account? Login here"
            ),
            uiState = RegisterUIState(),
            onFirstNameChanged = {},
            onLastNameChanged = {},
            onUsernameChanged = {},
            onEmailChanged = {},
            onPasswordChanged = {},
            onRetypePasswordChanged = {},
            onRegisterClick = {},
            onLoginClick = {}
        )
    }
}
