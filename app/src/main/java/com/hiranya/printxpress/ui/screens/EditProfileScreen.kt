package com.hiranya.printxpress.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material.icons.rounded.VisibilityOff
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hiranya.printxpress.viewmodel.ProfileViewModel
import com.hiranya.printxpress.ui.theme.Accent
import com.hiranya.printxpress.ui.theme.AccentContainer
import com.hiranya.printxpress.ui.theme.Background
import com.hiranya.printxpress.ui.theme.Divider
import com.hiranya.printxpress.ui.theme.OnAccent
import com.hiranya.printxpress.ui.theme.PrintXpressTheme
import com.hiranya.printxpress.ui.theme.StatusAmber
import com.hiranya.printxpress.ui.theme.StatusAmberBg
import com.hiranya.printxpress.ui.theme.TextPrimary
import com.hiranya.printxpress.data.entity.User
import com.hiranya.printxpress.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    val user by viewModel.user
    val updateError by viewModel.updateError
    val updateSuccess by viewModel.updateSuccess

    // Pop back automatically when save succeeds.
    LaunchedEffect(updateSuccess) {
        if (updateSuccess) {
            Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
            viewModel.updateSuccess.value = false
            navController.popBackStack()
        }
    }

    EditProfileContent(
        user = user,
        updateError = updateError,
        onBack = { navController.popBackStack() },
        onSave = { fullName, email, phone, currentPassword, newPassword, confirmPassword ->
            viewModel.updateProfile(
                fullName = fullName,
                email = email.ifBlank { null },
                phone = phone.ifBlank { null }
            )
            // Also change password if the current password field is filled.
            if (currentPassword.isNotBlank()) {
                viewModel.changePassword(currentPassword, newPassword, confirmPassword)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileContent(
    user: User?,
    updateError: String?,
    onBack: () -> Unit,
    onSave: (String, String, String, String, String, String) -> Unit
) {
    var fullName by remember(user) { mutableStateOf(user?.fullName ?: "") }
    var email by remember(user) { mutableStateOf(user?.email ?: "") }
    var phone by remember(user) { mutableStateOf(user?.phone ?: "") }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showCurrentPwd by remember { mutableStateOf(false) }
    var showNewPwd by remember { mutableStateOf(false) }
    var showConfirmPwd by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        onSave(fullName, email, phone, currentPassword, newPassword, confirmPassword)
                    }) {
                        Text("Save", color = Accent)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // Avatar with edit button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.size(100.dp)) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(AccentContainer, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(user?.fullName?.first()?.uppercase() ?: "?", style = MaterialTheme.typography.headlineMedium, color = Accent)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Text("Profile details", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
            Spacer(Modifier.height(8.dp))

            // Show save errors in red.
            if (updateError != null) {
                Text(updateError, style = MaterialTheme.typography.labelSmall, color = Color(0xFFC62828))
                Spacer(Modifier.height(4.dp))
            }

            // Personal info fields
            ProfileField(label = "Full name", value = fullName, onValueChange = { fullName = it })
            ProfileField(label = "Email", value = email, onValueChange = { email = it })
            ProfileField(label = "Phone", value = phone, onValueChange = { phone = it })

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = Divider)
            Spacer(Modifier.height(16.dp))

            Text("Change password", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
            Spacer(Modifier.height(8.dp))

            // Password fields with visibility toggles
            PasswordField(
                label = "Current password",
                value = currentPassword,
                onValueChange = { currentPassword = it },
                visible = showCurrentPwd,
                onToggle = { showCurrentPwd = !showCurrentPwd }
            )
            PasswordField(
                label = "New password",
                value = newPassword,
                onValueChange = { newPassword = it },
                visible = showNewPwd,
                onToggle = { showNewPwd = !showNewPwd }
            )
            PasswordField(
                label = "Confirm new password",
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                visible = showConfirmPwd,
                onToggle = { showConfirmPwd = !showConfirmPwd }
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

// Reusable text field for profile details
@Composable
private fun ProfileField(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Accent,
            unfocusedBorderColor = Divider,
            cursorColor = Accent,
            focusedLabelColor = Accent
        )
    )
}

// Reusable password field with show/hide toggle
@Composable
private fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    visible: Boolean,
    onToggle: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        singleLine = true,
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = onToggle) {
                Icon(
                    if (visible) Icons.Rounded.VisibilityOff else Icons.Rounded.Visibility,
                    contentDescription = if (visible) "Hide password" else "Show password",
                    tint = TextSecondary
                )
            }
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Accent,
            unfocusedBorderColor = Divider,
            cursorColor = Accent,
            focusedLabelColor = Accent
        )
    )
}

@Preview(showBackground = true)
@Composable
private fun EditProfileScreenPreview() {
    PrintXpressTheme {
        EditProfileContent(
            user = User(1, "Sarah Nilu", "sarah@nilu.lk", "0771234567", "", 0L),
            updateError = null,
            onBack = {},
            onSave = { _, _, _, _, _, _ -> }
        )
    }
}
