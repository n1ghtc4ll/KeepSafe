package com.example.cardioproject.core.common.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cardioproject.core.common.data.entity.TabataProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TabataProfileDao {
    @Query("SELECT * FROM tabata_profiles WHERE id = :id LIMIT 1")
    suspend fun getTabataProfileById(id: String): TabataProfileEntity?

    @Query("SELECT * FROM tabata_profiles")
    fun getAllTabataProfiles(): Flow<List<TabataProfileEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTabataProfile(tabata: TabataProfileEntity)
}