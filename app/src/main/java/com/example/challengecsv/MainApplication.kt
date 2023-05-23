package com.example.challengecsv

import android.app.Application
import androidx.room.Room
import com.example.challengecsv.data.AppDatabase
import com.example.challengecsv.data.MilkingDAO

const val DB_NAME = "milking-database"
class MainApplication : Application() {
    lateinit var milkingDAO: MilkingDAO
    private fun buildDatabase() : AppDatabase {
        return Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        milkingDAO = buildDatabase().milkingDAO()
    }
}