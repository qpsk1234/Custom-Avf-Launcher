package com.example.avflauncher.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.avflauncher.core.AvfManager
import com.example.avflauncher.core.VmConfig
import com.example.avflauncher.core.VmDatabase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class VmViewModel(application: Application) : AndroidViewModel(application) {
    private val db = VmDatabase.getDatabase(application)
    private val dao = db.vmDao()
    private val avfManager = AvfManager(application)

    val allVms: StateFlow<List<VmConfig>> = dao.getAllVmConfigs().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addVm(name: String, kernelPath: String, diskPath: String) {
        viewModelScope.launch {
            dao.insertVmConfig(
                VmConfig(
                    name = name,
                    kernelPath = kernelPath,
                    diskImagePath = diskPath
                )
            )
        }
    }

    fun updateVmJson(newJson: String, vm: VmConfig) {
        viewModelScope.launch {
            dao.insertVmConfig(vm.copy(configJson = newJson))
        }
    }

    fun startVm(config: VmConfig) {
        // AvfManagerを使用してVMを起動
        avfManager.createVm(config)?.start()
    }
}
