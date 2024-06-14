package com.bangkit.glowfyapp.data.historydatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(
    entities = [ScanHistory::class, ProfileEntity::class],
    version = 1
)
abstract class ScanHistoryDatabase: RoomDatabase() {

    companion object {
        private var Instance: ScanHistoryDatabase? = null

        fun getDatabase(context: Context): ScanHistoryDatabase? {
            if (Instance == null) {
                synchronized(ScanHistoryDatabase::class.java) {
                    Instance = Room.databaseBuilder(context.applicationContext, ScanHistoryDatabase::class.java, "scan_history").build()
                }
            }
            return Instance
        }
    }
    abstract fun scanHistoryDao(): ScanHistoryDao
    abstract fun profileDao(): ProfileDao
}