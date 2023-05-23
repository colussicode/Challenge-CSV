package com.example.challengecsv.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [MilkingItem::class, DailyControlItem::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun milkingDAO() : MilkingDAO
}