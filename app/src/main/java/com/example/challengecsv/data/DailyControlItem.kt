package com.example.challengecsv.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DailyControlItem(
    @PrimaryKey(autoGenerate = true) val id : Int = 0,
    val stringId: String,
    val microchip: String,
    val animalNumber: String,
    val name: String,
    val birthDate: String,
    val bay: String,
    val firstMilking: String,
    val secondMilking: String,
    val total: String,
    val controlDate: String,
    val del: String,
)
