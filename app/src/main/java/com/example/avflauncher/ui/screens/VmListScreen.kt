package com.example.avflauncher.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.avflauncher.core.VmConfig

@Composable
fun VmListScreen(
    vmList: List<VmConfig>,
    onAddVm: () -> Unit,
    onStartVm: (VmConfig) -> Unit,
    onEditConfig: (VmConfig) -> Unit
) {
    Scaffold(
        topBar = {
            SmallTopAppBar(title = { Text("Custom AVF Launcher") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddVm) {
                Icon(Icons.Default.Add, contentDescription = "Add VM")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(vmList) { vm ->
                VmCard(
                    vm = vm,
                    onStart = { onStartVm(vm) },
                    onEdit = { onEditConfig(vm) }
                )
            }
        }
    }
}

@Composable
fun VmCard(
    vm: VmConfig,
    onStart: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = vm.name, style = MaterialTheme.typography.titleMedium)
                Text(text = "MEM: ${vm.memoryMb}MB | CPU: ${vm.cpuCount}", style = MaterialTheme.typography.bodySmall)
            }
            Row {
                IconButton(onClick = onEdit) {
                    Icon(Icons.Default.Settings, contentDescription = "Edit JSON")
                }
                IconButton(onClick = onStart) {
                    Icon(Icons.Default.PlayArrow, contentDescription = "Start VM")
                }
            }
        }
    }
}
