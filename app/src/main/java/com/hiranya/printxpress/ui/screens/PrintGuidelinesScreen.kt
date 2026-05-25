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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hiranya.printxpress.ui.theme.Accent
import com.hiranya.printxpress.ui.theme.AccentContainer
import com.hiranya.printxpress.ui.theme.Background
import com.hiranya.printxpress.ui.theme.Divider
import com.hiranya.printxpress.ui.theme.PrintXpressTheme
import com.hiranya.printxpress.ui.theme.TextPrimary
import com.hiranya.printxpress.ui.theme.TextSecondary

// Guideline topic data
private data class Guideline(val title: String, val body: String)

private val guidelines = listOf(
    Guideline(
        "Accepted file types",
        "We accept PDF, PNG, and JPG files. PDF is preferred for print because it preserves fonts, colours, and layout exactly. PNG works well for logos and artwork with transparent backgrounds."
    ),
    Guideline(
        "Resolution requirements",
        "Files must be at least 300 DPI for sharp print results. Images from websites are usually only 72 DPI and will print blurry. Always export at 300 DPI or higher from your design tool."
    ),
    Guideline(
        "Colour mode",
        "Set your file to CMYK colour mode before exporting. RGB files will be converted automatically but the printed colours may look slightly different. Bright neon or screen colours often shift when printed."
    ),
    Guideline(
        "Bleed and margins",
        "Add a 3 mm bleed on all sides so the design extends past the cut line. Keep important text and logos at least 5 mm from the edge. This prevents accidental cropping when the sheet is trimmed."
    ),
    Guideline(
        "How to prepare your file",
        "Flatten all layers and embed all linked images before saving. Outline any text that uses custom fonts so the print file is self-contained. Save as PDF/X-1a for the best compatibility."
    )
)

@Composable
fun PrintGuidelinesScreen(navController: NavController) {
    PrintGuidelinesContent(onBack = { navController.popBackStack() })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrintGuidelinesContent(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Print guidelines") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Info banner at the top
            item {
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = AccentContainer),
                    elevation = CardDefaults.cardElevation(0.dp),
                    border = BorderStroke(0.dp, AccentContainer)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(Icons.Rounded.Info, contentDescription = null, tint = Accent)
                        Text(
                            "Follow these guidelines to get the best possible print every time. If unsure, our team will review your file before printing.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextPrimary
                        )
                    }
                }
            }

            // Expandable guideline topics
            items(guidelines) { guideline ->
                GuidelineAccordion(guideline)
            }
        }
    }
}

@Composable
private fun GuidelineAccordion(guideline: Guideline) {
    var expanded by remember { mutableStateOf(false) }
    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "chevron"
    )

    Column {
        // Clickable header row
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
                guideline.title,
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
            // Body text shown when expanded
            Column(modifier = Modifier.background(AccentContainer)) {
                Text(
                    guideline.body,
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
private fun PrintGuidelinesScreenPreview() {
    PrintXpressTheme {
        PrintGuidelinesContent(onBack = {})
    }
}
