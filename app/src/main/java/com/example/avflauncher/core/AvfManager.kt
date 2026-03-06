package com.example.avflauncher.core

import android.content.Context
import android.system.virtualmachine.VirtualMachine
import android.system.virtualmachine.VirtualMachineConfig
import android.system.virtualmachine.VirtualMachineCustomImageConfig
import android.system.virtualmachine.VirtualMachineDisplayConfig
import android.system.virtualmachine.VirtualMachineManager
import android.system.virtualmachine.VirtioGpuConfig
import java.io.File
import java.io.FileInputStream

class AvfManager(private val context: Context) {

    private val vmManager: VirtualMachineManager? by lazy {
        context.getSystemService(VirtualMachineManager::class.java)
    }

    /**
     * pKVMが有効かどうかを確認する
     */
    fun isPkvmSupported(): Boolean {
        // 実際のAPIでは VirtualMachineManager の capabilities 等を確認する想定
        // Android 16のプレビュー仕様に基づき実装
        return vmManager?.isFeatureSupported(VirtualMachineManager.FEATURE_PROTECTED_VM) ?: false
    }

    /**
     * 指定された設定で仮想マシンを作成・構成する
     */
    fun createVm(config: VmConfig): VirtualMachine? {
        val manager = vmManager ?: return null
        
        val kernelFile = File(config.kernelPath)
        val diskFile = File(config.diskImagePath)
        
        if (!kernelFile.exists() || !diskFile.exists()) {
            return null
        }

        val vmConfigBuilder = VirtualMachineConfig.Builder(context)
            .setMemoryBytes(config.memoryMb * 1024L * 1024L)
            .setCpuCount(config.cpuCount)
            .setProtectedVm(true) // pKVM要件

        // カスタムイメージの設定
        val customImageConfig = VirtualMachineCustomImageConfig.Builder()
            .setOsImageFileDescriptor(FileInputStream(diskFile).fd)
            .build()
        
        vmConfigBuilder.setVirtualMachineCustomImageConfig(customImageConfig)

        // Android 16: vm_config.json の直接適用 (イメージパスが指定されている場合)
        if (config.configJson.isNotEmpty()) {
            // 注意: 実際には暫定ファイルに書き出して読み込ませる等の処理が必要になる場合がある
            // ここではAPIの意図を汲んでプレースホルダとして実装
            // vmConfigBuilder.setCustomConfigJson(config.configJson)
        }

        // GPU / ディスプレイ設定 (Android 16+)
        if (config.useGpu) {
            val displayConfig = VirtualMachineDisplayConfig.Builder()
                .setGpuConfig(VirtioGpuConfig.Builder().build())
                .setResolution(1024, 768)
                .build()
            vmConfigBuilder.setVirtualMachineDisplayConfig(displayConfig)
        }

        manager.create(config.name, vmConfigBuilder.build())
    }
}
