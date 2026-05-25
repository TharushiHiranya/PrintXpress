package com.hiranya.printxpress.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hiranya.printxpress.data.entity.Address
import com.hiranya.printxpress.viewmodel.ProfileViewModel
import com.hiranya.printxpress.ui.theme.Accent
import com.hiranya.printxpress.ui.theme.AccentContainer
import com.hiranya.printxpress.ui.theme.Background
import com.hiranya.printxpress.ui.theme.Divider
import com.hiranya.printxpress.ui.theme.OnAccent
import com.hiranya.printxpress.ui.theme.PrintXpressTheme
import com.hiranya.printxpress.ui.theme.TextPrimary
import com.hiranya.printxpress.ui.theme.TextSecondary
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height

@Composable
fun AddressesScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    val addresses by viewModel.addresses
    var showAddDialog by remember { mutableStateOf(false) }

    AddressesContent(
        addresses = addresses,
        onBack = { navController.popBackStack() },
        onDelete = { viewModel.deleteAddress(it) },
        onSetDefault = { viewModel.setDefaultAddress(it) },
        onAddAddress = { showAddDialog = true }
    )

    if (showAddDialog) {
        AddAddressDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { label, line1, city, postalCode ->
                viewModel.addAddress(label, line1, city, postalCode)
                showAddDialog = false
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddressesContent(
    addresses: List<Address>,
    onBack: () -> Unit,
    onDelete: (Long) -> Unit,
    onSetDefault: (Long) -> Unit,
    onAddAddress: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Delivery addresses") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Existing address cards from ViewModel
            items(addresses) { addr ->
                AddressCard(
                    addr = addr,
                    onDelete = { onDelete(addr.addressId) },
                    onSetDefault = { onSetDefault(addr.addressId) }
                )
            }

            // Add new address card
            item {
                OutlinedCard(
                    onClick = onAddAddress,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Background),
                    elevation = CardDefaults.cardElevation(0.dp),
                    border = BorderStroke(1.5.dp, Accent)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Accent, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Rounded.Add, contentDescription = null, tint = OnAccent, modifier = Modifier.size(20.dp))
                        }
                        Text("Add new address", style = MaterialTheme.typography.bodyLarge, color = Accent)
                    }
                }
            }
        }
    }
}

@Composable
private fun AddressCard(
    addr: Address,
    onDelete: () -> Unit = {},
    onSetDefault: () -> Unit = {}
) {
    OutlinedCard(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Divider),
        colors = CardDefaults.cardColors(containerColor = Background),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Name row with optional default badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(AccentContainer, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Rounded.LocationOn, contentDescription = null, tint = Accent, modifier = Modifier.size(18.dp))
                    }
                    Text(addr.label, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                    if (addr.isDefault) {
                        Surface(shape = RoundedCornerShape(50.dp), color = AccentContainer) {
                            Text(
                                "Default",
                                style = MaterialTheme.typography.labelSmall,
                                color = Accent,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            Text("${addr.line1}, ${addr.city}", style = MaterialTheme.typography.bodyMedium, color = TextPrimary)
            Text("Postal Code: ${addr.postalCode}", style = MaterialTheme.typography.labelSmall, color = TextSecondary)

            HorizontalDivider(color = Divider)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onDelete) {
                    Text("Delete", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }
                if (!addr.isDefault) {
                    TextButton(onClick = onSetDefault) {
                        Text("Set as default", style = MaterialTheme.typography.bodyMedium, color = Accent)
                    }
                }
            }
        }
    }
}

@Composable
fun AddAddressDialog(
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var label by remember { mutableStateOf("") }
    var line1 by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var postalCode by remember { mutableStateOf("") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Background,
            tonalElevation = 2.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Add new address",
                    style = MaterialTheme.typography.headlineSmall,
                    color = TextPrimary
                )

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AddressInputField(label = "Address Label (e.g. Home, Office)", value = label, onValueChange = { label = it })
                    AddressInputField(label = "Address Line", value = line1, onValueChange = { line1 = it })
                    AddressInputField(label = "City", value = city, onValueChange = { city = it })
                    AddressInputField(label = "Postal Code", value = postalCode, onValueChange = { postalCode = it })
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Divider)
                    ) {
                        Text("Cancel", color = TextSecondary)
                    }
                    Button(
                        onClick = {
                            if (label.isNotBlank() && line1.isNotBlank() && city.isNotBlank() && postalCode.isNotBlank()) {
                                onConfirm(label, line1, city, postalCode)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Accent),
                        enabled = label.isNotBlank() && line1.isNotBlank() && city.isNotBlank() && postalCode.isNotBlank()
                    ) {
                        Text("Save", color = OnAccent)
                    }
                }
            }
        }
    }
}

@Composable
private fun AddressInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
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

@Preview(showBackground = true)
@Composable
private fun AddressesScreenPreview() {
    PrintXpressTheme {
        AddressesContent(
            addresses = listOf(
                Address(addressId = 1, userId = 1, label = "Home", line1 = "42/3 Galle Road", city = "Colombo 04", postalCode = "00400", isDefault = true),
                Address(addressId = 2, userId = 1, label = "Studio", line1 = "18 Horton Pl", city = "Colombo 07", postalCode = "00700", isDefault = false)
            ),
            onBack = {},
            onDelete = {},
            onSetDefault = {},
            onAddAddress = {}
        )
    }
}
