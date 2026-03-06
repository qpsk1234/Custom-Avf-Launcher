package com.example.avflauncher.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.avflauncher.ui.theme.AvfLauncherTheme

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.avflauncher.ui.theme.AvfLauncherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AvfLauncherTheme {
                val viewModel: VmViewModel = viewModel()
                val vms by viewModel.allVms.collectAsState()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AvfNavHost(
                        vmList = vms,
                        onAddVm = {
                            // デモ用に適当な値を追加
                            viewModel.addVm("New VM", "/storage/emulated/0/AVF/images/kernel", "/storage/emulated/0/AVF/images/disk.img")
                        },
                        onStartVm = { viewModel.startVm(it) },
                        onSaveJson = { newJson, vm ->
                            viewModel.updateVmJson(newJson, vm)
                        }
                    )
                }
            }
        }
    }
}
