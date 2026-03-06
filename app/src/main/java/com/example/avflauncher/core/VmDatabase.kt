package com.example.avflauncher.core

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface VmDao {
    @Query("SELECT * FROM vm_configs")
    fun getAllVmConfigs(): Flow<List<VmConfig>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVmConfig(config: VmConfig)

    @Delete
    suspend fun deleteVmConfig(config: VmConfig)

    @Query("SELECT * FROM vm_configs WHERE id = :id")
    suspend fun getVmConfigById(id: String): VmConfig?
}

@Database(entities = [VmConfig::class], version = 1)
abstract class VmDatabase : RoomDatabase() {
    abstract fun vmDao(): VmDao

    companion object {
        @Volatile
        private var INSTANCE: VmDatabase? = null

        fun getDatabase(context: android.content.Context): VmDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    VmDatabase::class.java,
                    "vm_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
