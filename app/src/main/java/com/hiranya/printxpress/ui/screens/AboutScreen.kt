package com.hiranya.printxpress.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.hiranya.printxpress.R
import com.hiranya.printxpress.ui.theme.Background
import com.hiranya.printxpress.ui.theme.PrintXpressTheme
import com.hiranya.printxpress.ui.theme.TextPrimary
import com.hiranya.printxpress.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("About PrintXpress") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
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
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "PrintXpress Logo",
                modifier = Modifier.height(60.dp),
                contentScale = ContentScale.Fit
            )
            
            Spacer(Modifier.height(32.dp))
            
            Text(
                "Your local digital printing partner.",
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            
            Spacer(Modifier.height(16.dp))
            
            Text(
                "PrintXpress provides high-quality digital printing services in Sri Lanka. We specialize in business cards, posters, banners, and custom gifts like mugs and t-shirts. Our goal is to make professional printing fast and accessible for everyone.",
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            
            Spacer(Modifier.height(24.dp))
            
            Text(
                "Why choose us?",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            
            Spacer(Modifier.height(8.dp))
            
            Text(
                "We use modern equipment to ensure vibrant colors and sharp details. You can upload your designs directly through this app or use our custom text tools. We offer both home delivery and store pickup to suit your schedule.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.height(48.dp))
            
            Text(
                "Version 1.0.0",
                style = MaterialTheme.typography.labelMedium,
                color = TextSecondary
            )
            
            Text(
                "Built for PrintXpress Digital Services",
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AboutScreenPreview() {
    PrintXpressTheme {
        AboutScreen(rememberNavController())
    }
}
