package com.example.avflauncher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VmJsonEditorScreen(
    initialJson: String,
    onSave: (String) -> Unit,
    onBack: () -> Unit
) {
    var jsonText by remember { mutableStateOf(initialJson) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "vm_config.json Editor",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        OutlinedTextField(
            value = jsonText,
            onValueChange = { jsonText = it },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            label = { Text("Config JSON") },
            textStyle = MaterialTheme.typography.bodyMedium
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = onBack) {
                Text("Cancel")
            }
            Button(onClick = { onSave(jsonText) }) {
                Text("Save")
            }
        }
    }
}
