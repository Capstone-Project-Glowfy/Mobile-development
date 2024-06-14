package com.bangkit.glowfyapp.data.historydatabase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ScanHistoryDao {

    @Insert
    suspend fun addScanToHistory(scanHistory: ScanHistory)

    @Query("SELECT * FROM scan_history")
    suspend fun getScanHistory(): List<ScanHistory>

    @Query("DELETE FROM scan_history WHERE scan_history.id = :id")
    suspend fun clearScanHistory(id: Int): Int

}

@Dao
interface ProfileDao {

    @Insert
    suspend fun addToProfile(profile: ProfileEntity)

    @Query("SELECT * FROM profile LIMIT 1")
    suspend fun getProfile(): ProfileEntity?

    @Query("DELETE FROM profile")
    suspend fun deleteProfile()
}