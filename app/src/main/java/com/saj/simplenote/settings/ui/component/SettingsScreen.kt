package com.saj.simplenote.settings.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.saj.simplenote.R
import com.saj.simplenote.domain.ui.theme.MyGreyDivider
import com.saj.simplenote.domain.ui.theme.MyPurple
import com.saj.simplenote.domain.ui.theme.MyWhite
import com.saj.simplenote.domain.ui.theme.SimpleNoteTheme
import com.saj.simplenote.settings.ui.model.SettingsUIModel
import com.saj.simplenote.settings.ui.model.SettingsUIState

@Composable
fun SettingsScreen(
    data: SettingsUIModel,
    uiState: SettingsUIState,
    onBackClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onLogOutClick: () -> Unit,
    onLogoutDialogCancel: () -> Unit,
    onLogoutDialogConfirm: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            // Top bar with back button and title
            SettingsTopAppBar(
                title = data.title,
                onBackClick = onBackClick
            )

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = MyGreyDivider
            )

            Spacer(modifier = Modifier.height(30.dp))

            // Profile Section
            ProfileSection(
                userName = uiState.userName,
                userEmail = uiState.userEmail
            )

            Spacer(modifier = Modifier.height(20.dp))

            HorizontalDivider(
                thickness = 1.dp,
                color = MyGreyDivider,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            Spacer(modifier = Modifier.height(50.dp))

            // App Settings Header
            Text(
                text = "APP SETTINGS",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = Color.Gray,
                    fontWeight = FontWeight.Normal,
                    fontSize = 13.sp,
                    letterSpacing = 0.5.sp
                ),
                modifier = Modifier.padding(start = 24.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Change Password Option
            SettingsItem(
                icon = R.drawable.ic_lock,
                iconDesc = "Lock Icon",
                title = data.changePasswordTitle,
                onClick = onChangePasswordClick
            )
            Spacer(modifier = Modifier.height(10.dp))

            HorizontalDivider(
                thickness = 1.dp,
                color = MyGreyDivider,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            // Log Out Option
            SettingsItem(
                icon = R.drawable.ic_logout,
                iconDesc = "Logout Icon",
                title = data.logOutTitle,
                onClick = onLogOutClick,
                isDestructive = true // To color it red and remove the chevron
            )
        }

        // Version text at the bottom
        Text(
            text = stringResource(id = R.string.version_name),
            color = Color.LightGray,
            fontSize = 12.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
        )

        // Loading overlay
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.8f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MyPurple,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        // Logout Confirmation Dialog
        if (uiState.showLogoutDialog) {
            LogoutConfirmationDialog(
                title = data.logoutDialogTitle,
                message = data.logoutDialogMessage,
                cancelText = data.logoutDialogCancel,
                confirmText = data.logoutDialogConfirm,
                onCancel = onLogoutDialogCancel,
                onConfirm = onLogoutDialogConfirm
            )
        }
    }
}

@Composable
private fun SettingsTopAppBar(title: String, onBackClick: () -> Unit) {
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


@Composable
private fun ProfileSection(userName: String, userEmail: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            // NOTE: Replace with your actual profile image resource
            painter = painterResource(id = R.drawable.ic_setting_logo),
            contentDescription = "Profile Picture",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(0.5.dp, Color.LightGray.copy(alpha = 0.5f), CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = userName,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    // NOTE: Replace with your actual email icon resource
                    painter = painterResource(id = R.drawable.ic_email),
                    contentDescription = "Email icon",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = userEmail,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                )
            }
        }
    }
}

@Composable
private fun SettingsItem(
    icon: Int,
    iconDesc: String,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isDestructive: Boolean = false,
) {
    val contentColor = if (isDestructive) Color(0xFFD32F2F) else Color.Black
    val iconTint = if (isDestructive) Color(0xFFD32F2F) else Color(0xFF434343)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 24.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = iconDesc,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 17.sp,
                color = contentColor
            ),
            modifier = Modifier.weight(1f)
        )

        if (!isDestructive) {
            Icon(
                painter = painterResource(id = R.drawable.ic_simple_arrow_back),
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier.size(16.dp).rotate(180f)
            )
        }
    }
}

@Composable
private fun LogoutConfirmationDialog(
    title: String,
    message: String,
    cancelText: String,
    confirmText: String,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(dismissOnClickOutside = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MyWhite,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(35.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Message
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        color = Color(0xFF666666),
                        lineHeight = 24.sp
                    ),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Cancel Button
                    OutlinedButton(
                        onClick = onCancel,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = MaterialTheme.shapes.extraLarge,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            Color(0xFF504EC3)
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color(0xFF504EC3)
                        )
                    ) {
                        Text(
                            text = cancelText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Confirm Button
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = MaterialTheme.shapes.extraLarge,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MyPurple,
                            contentColor = MyWhite
                        )
                    ) {
                        Text(
                            text = confirmText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, widthDp = 390, heightDp = 844) // iPhone 13 Pro dimensions
@Composable
fun SettingsScreenPreview() {
    SimpleNoteTheme {
        // Mock data to match the provided image
        SettingsScreen(
            data = SettingsUIModel(
                title = "Settings",
                changePasswordTitle = "Change Password",
                logOutTitle = "Log Out",
                logoutDialogTitle = "Log Out",
                logoutDialogMessage = "Are you sure you want to log out from the application?",
                logoutDialogCancel = "Cancel",
                logoutDialogConfirm = "Yes"
            ),
            uiState = SettingsUIState(
                userName = "Ali Jafari",
                userEmail = "jafari.ali@gmail.com"
            ),
            onBackClick = {},
            onChangePasswordClick = {},
            onLogOutClick = {},
            onLogoutDialogCancel = {},
            onLogoutDialogConfirm = {}
        )
    }
}