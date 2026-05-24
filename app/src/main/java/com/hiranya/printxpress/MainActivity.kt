package com.hiranya.printxpress

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.hiranya.printxpress.ui.PrintXpressNavGraph
import com.hiranya.printxpress.ui.theme.PrintXpressTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrintXpressTheme {
                // Entry point for all screens. Navigation is handled inside PrintXpressNavGraph.
                PrintXpressNavGraph()
            }
        }
    }
}