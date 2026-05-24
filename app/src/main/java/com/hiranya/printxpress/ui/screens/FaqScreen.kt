package com.hiranya.printxpress.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hiranya.printxpress.ui.theme.Accent
import com.hiranya.printxpress.ui.theme.AccentContainer
import com.hiranya.printxpress.ui.theme.Background
import com.hiranya.printxpress.ui.theme.Divider
import com.hiranya.printxpress.ui.theme.PrintXpressTheme
import com.hiranya.printxpress.ui.theme.TextDisabled
import com.hiranya.printxpress.ui.theme.TextPrimary
import com.hiranya.printxpress.ui.theme.TextSecondary

// FAQ item data
private data class FaqItem(val question: String, val answer: String)

private val faqs = listOf(
    FaqItem(
        "How long does printing take?",
        "Most orders are printed within 1–3 business days. Express orders are ready the same day if placed before noon. We'll send a notification when your order is ready."
    ),
    FaqItem(
        "Can I cancel after placing an order?",
        "You can cancel or make changes while the order is still in Processing status. Once printing starts, changes are not possible. Open your order and tap Cancel."
    ),
    FaqItem(
        "What payment methods do you accept?",
        "We accept cash on pickup and cash on delivery. Online payment support is coming soon."
    ),
    FaqItem(
        "Do you ship outside Colombo?",
        "Yes, we deliver island-wide via our courier partners. Delivery times and fees vary by area. Enter your address at checkout to see the options."
    ),
    FaqItem(
        "How do I get a quote for bulk orders?",
        "For orders over LKR 50,000 or more than 1,000 pieces, contact us directly at hello@printxpress.lk for a custom quote and priority handling."
    ),
    FaqItem(
        "Are my designs kept private?",
        "Yes. We never share, reprint, or use your files for any purpose other than your order. Files are deleted from our servers 30 days after delivery."
    )
)

// Contact channel data
private data class ContactItem(val icon: ImageVector, val label: String, val value: String, val sub: String)

private val contactItems = listOf(
    ContactItem(Icons.Rounded.Phone, "Phone", "+94 11 555 0142", "Mon–Sat, 9am–7pm"),
    ContactItem(Icons.Rounded.Email, "Email", "hello@printxpress.lk", "We reply within a day"),
    ContactItem(Icons.Rounded.LocationOn, "Store", "42 Stratford Ave, Colombo 06", "Bambalapitiya · Open today")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaqScreen(navController: NavController) {
    FaqContent(onBack = { navController.popBackStack() })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaqContent(onBack: () -> Unit) {
    var searchQuery by remember { mutableStateOf("") }

    // Filter FAQs based on search input
    val filteredFaqs = if (searchQuery.isBlank()) faqs
    else faqs.filter { it.question.contains(searchQuery, ignoreCase = true) || it.answer.contains(searchQuery, ignoreCase = true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FAQs and support") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
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
        ) {
            // Search field
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(50.dp),
                placeholder = { Text("Search FAQs...", color = TextDisabled) },
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null, tint = TextSecondary) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Accent,
                    unfocusedBorderColor = Divider,
                    cursorColor = Accent,
                    focusedLabelColor = Accent
                )
            )

            // FAQ accordion list + contact section
            LazyColumn(contentPadding = PaddingValues(bottom = 24.dp)) {
                items(filteredFaqs) { faq ->
                    FaqAccordion(faq)
                }

                // Contact section
                item {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "Contact us",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                }

                items(contactItems) { contact ->
                    OutlinedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 4.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, Divider),
                        colors = CardDefaults.cardColors(containerColor = Background),
                        elevation = CardDefaults.cardElevation(0.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(AccentContainer, RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(contact.icon, contentDescription = null, tint = Accent)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(contact.label, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
                                Text(contact.value, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                                Text(contact.sub, style = MaterialTheme.typography.labelSmall, color = TextDisabled)
                            }
                            Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = Accent)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FaqAccordion(faq: FaqItem) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "chevron"
    )

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = !expanded }
                .background(if (expanded) AccentContainer else Background)
                .padding(horizontal = 16.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                faq.question,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            Icon(
                Icons.Rounded.ExpandMore,
                contentDescription = null,
                tint = Accent,
                modifier = Modifier.rotate(rotation)
            )
        }

        if (expanded) {
            Column(modifier = Modifier.background(AccentContainer)) {
                Text(
                    faq.answer,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier.padding(16.dp)
                )
                HorizontalDivider(color = Divider)
            }
        } else {
            HorizontalDivider(color = Divider)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FaqScreenPreview() {
    PrintXpressTheme {
        FaqContent(onBack = {})
    }
}
