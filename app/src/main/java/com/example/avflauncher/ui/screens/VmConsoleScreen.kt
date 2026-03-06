package com.example.avflauncher.ui.screens

import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.avflauncher.core.VmConfig

@Composable
fun VmConsoleScreen(
    vm: VmConfig,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("VM: ${vm.name}") },
                navigationIcon = {
                    TextButton(onClick = onBack) {
                        Text("Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            AndroidView(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                factory = { context ->
                    SurfaceView(context).apply {
                        holder.addCallback(object : SurfaceHolder.Callback {
                            override fun surfaceCreated(holder: SurfaceHolder) {
                                // TODO: VirtualMachine.setSurface(holder.surface) を呼ぶ
                                // Android 16の実際のAPIに合わせて実装
                            }
                            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}
                            override fun surfaceDestroyed(holder: SurfaceHolder) {}
                        })
                    }
                }
            )
            
            // 下部に制御ボタン等
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { /* TODO: Send Ctrl+C or similar */ }) {
                    Text("Ctrl+C")
                }
                Button(onClick = { /* TODO: Send Enter */ }) {
                    Text("Enter")
                }
            }
        }
    }
}
