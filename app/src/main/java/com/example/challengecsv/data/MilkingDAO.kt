package com.example.challengecsv.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.google.android.material.circularreveal.CircularRevealHelper.Strategy

@Dao
interface MilkingDAO {

    @Query("SELECT * FROM milkingitem")
    fun listMilkItemFromDatabase() : List<MilkingItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMilkItemToDatabase(list: List<MilkingItem>)

    @Insert
    fun insertDailyControlItemToDatabase(list: List<DailyControlItem>)

    @Query("SELECT * FROM dailycontrolitem")
    fun listDailyControlItemFromDatabase() : List<DailyControlItem>
}