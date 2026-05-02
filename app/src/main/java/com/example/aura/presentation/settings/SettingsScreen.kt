package com.example.aura.presentation.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen() {
    var isCloudMode by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("AI Mode", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Use Cloud AI (Gemini API Fallback)", modifier = Modifier.weight(1f))
                Switch(checked = isCloudMode, onCheckedChange = { isCloudMode = it })
            }
            if (!isCloudMode) {
                Text("AICore Status: Available on this device ✓", color = MaterialTheme.colorScheme.primary)
            } else {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("Gemini API Key") },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            Text("Preferences", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            OutlinedTextField(
                value = "Gayanthika",
                onValueChange = {},
                label = { Text("Your Name (for message signing)") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { /* Export */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Export all meetings as JSON")
            }
            OutlinedButton(onClick = { /* Clear Data */ }, modifier = Modifier.fillMaxWidth()) {
                Text("Clear all data", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
