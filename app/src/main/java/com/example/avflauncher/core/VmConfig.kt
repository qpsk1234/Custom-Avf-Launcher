package com.example.avflauncher.core

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "vm_configs")
data class VmConfig(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val kernelPath: String,
    val diskImagePath: String,
    val configJson: String = "",
    val customParams: String = "",
    val cpuCount: Int = 1,
    val memoryMb: Int = 1024,
    val diskSizeGb: Int = 10,
    val useGpu: Boolean = true
)
