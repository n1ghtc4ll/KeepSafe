package com.example.cardioproject.anthrodiary.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cardioproject.anthrodiary.data.local.dao.AnthroMeasurementDao
import com.example.cardioproject.anthrodiary.data.local.entity.AnthroMeasurementEntity

/*
@Database(entities = [AnthroMeasurementEntity::class], version = 1, exportSchema = false)
abstract class AnthroDiaryDatabase : RoomDatabase() {

    abstract fun anthroMeasurementDao(): AnthroMeasurementDao

    companion object {
        private const val DATABASE_NAME = "anthro_diary.db"

        @Volatile
        private var instance: AnthroDiaryDatabase? = null

        fun getInstance(context: Context): AnthroDiaryDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AnthroDiaryDatabase::class.java,
                    DATABASE_NAME
                ).build().also { instance = it }
            }
    }
}*/
