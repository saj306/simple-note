package com.saj.simplenote.changepassword.ui.component

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
import com.saj.simplenote.changepassword.ui.model.ChangePasswordUIModel
import com.saj.simplenote.changepassword.ui.model.ChangePasswordUIState
import com.saj.simplenote.domain.ui.theme.MyGreyDivider
import com.saj.simplenote.domain.ui.theme.MyPurple
import com.saj.simplenote.domain.ui.theme.MyWhite
import com.saj.simplenote.domain.ui.theme.SimpleNoteTheme

@Composable
fun ChangePasswordScreen(
    data: ChangePasswordUIModel,
    uiState: ChangePasswordUIState,
    onCurrentPasswordChanged: (String) -> Unit,
    onNewPasswordChanged: (String) -> Unit,
    onRetypeNewPasswordChanged: (String) -> Unit,
    onSubmitClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Use separate remember blocks with unique keys to prevent state conflicts
    var currentPasswordVisible by remember(key1 = "current_password_visibility") { mutableStateOf(false) }
    var newPasswordVisible by remember(key1 = "new_password_visibility") { mutableStateOf(false) }
    var retypePasswordVisible by remember(key1 = "retype_password_visibility") { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MyWhite)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Scrollable content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(scrollState)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Top bar with back button and title
                ChangePasswordTopAppBar(
                    title = data.title,
                    onBackClick = onBackClick
                )

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(
                    thickness = 1.dp,
                    color = MyGreyDivider
                )

                Spacer(modifier = Modifier.height(40.dp))

                // Form content
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {

                    Text(
                        text = stringResource(id = R.string.change_password_current_password_description),
                        color = MaterialTheme.colorScheme.onTertiary, // Green color for success
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )

                    // Current Password Field
                    PasswordField(
                        value = uiState.currentPassword,
                        onValueChange = onCurrentPasswordChanged,
                        label = data.currentPasswordLabel,
                        placeholder = data.currentPasswordPlaceholder,
                        passwordVisible = currentPasswordVisible,
                        onToggleVisibility = { currentPasswordVisible = !currentPasswordVisible }
                    )

                    HorizontalDivider(
                        thickness = 1.dp,
                        color = MyGreyDivider,
                    )

                    Text(
                        text = stringResource(id = R.string.change_password_new_password_description),
                        color = MaterialTheme.colorScheme.onTertiary, // Green color for success
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )

                    // New Password Field
                    PasswordField(
                        value = uiState.newPassword,
                        onValueChange = onNewPasswordChanged,
                        label = data.newPasswordLabel,
                        placeholder = data.newPasswordPlaceholder,
                        passwordVisible = newPasswordVisible,
                        onToggleVisibility = { newPasswordVisible = !newPasswordVisible }
                    )

                    // Retype New Password Field
                    PasswordField(
                        value = uiState.retypeNewPassword,
                        onValueChange = onRetypeNewPasswordChanged,
                        label = data.retypeNewPasswordLabel,
                        placeholder = data.retypeNewPasswordPlaceholder,
                        passwordVisible = retypePasswordVisible,
                        onToggleVisibility = { retypePasswordVisible = !retypePasswordVisible }
                    )

                    // Success message
                    uiState.successMessage?.let { message ->
                        Text(
                            text = message,
                            color = Color(0xFF4CAF50), // Green color for success
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }

                    // Error message
                    uiState.errorMessage?.let { error ->
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

            // Submit Button at the bottom
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 45.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .clickable(
                            onClick = onSubmitClick,
                            enabled = !uiState.isLoading
                        )
                        .fillMaxWidth()
                        .height(56.dp)
                        .padding(horizontal = 20.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            color = MyWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    } else {
                        Text(
                            text = data.submitButtonText,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            ),
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            textAlign = TextAlign.Center
                        )

                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_right),
                            contentDescription = "Submit arrow",
                            modifier = Modifier
                                .size(16.dp)
                                .align(Alignment.CenterEnd),
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    passwordVisible: Boolean,
    onToggleVisibility: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp,
                color = Color.Black
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color.Gray
                    )
                )
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                Icon(
                    painter = painterResource(
                        id = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                    ),
                    contentDescription = if (passwordVisible) "Hide password" else "Show password",
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onToggleVisibility() }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MyPurple,
                unfocusedBorderColor = Color.LightGray,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                cursorColor = MyPurple
            )
        )
    }
}

@Composable
private fun ChangePasswordTopAppBar(title: String, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp, bottom = 10.dp, start = 8.dp, end = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Color.Black
            ),
            textAlign = TextAlign.Center
        )
        Row(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = 16.dp)
                .clickable { onBackClick() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_simple_arrow_back),
                contentDescription = "Back",
                tint = MyPurple,
                modifier = Modifier.size(10.dp)
            )
            Text(
                text = "Back",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = MyPurple,
                    fontSize = 17.sp,
                ),
                modifier = Modifier.padding(start = 15.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun ChangePasswordScreenPreview() {
    SimpleNoteTheme {
        ChangePasswordScreen(
            data = ChangePasswordUIModel(
                title = "Change Password",
                currentPasswordLabel = "Current Password",
                newPasswordLabel = "New Password",
                retypeNewPasswordLabel = "Retype New Password",
                currentPasswordPlaceholder = "********",
                newPasswordPlaceholder = "********",
                retypeNewPasswordPlaceholder = "********",
                submitButtonText = "Submit New Password",
                backButtonText = "Back"
            ),
            uiState = ChangePasswordUIState(),
            onCurrentPasswordChanged = {},
            onNewPasswordChanged = {},
            onRetypeNewPasswordChanged = {},
            onSubmitClick = {},
            onBackClick = {}
        )
    }
}