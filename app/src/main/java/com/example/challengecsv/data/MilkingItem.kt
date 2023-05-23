package com.example.challengecsv.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MilkingItem(
    @PrimaryKey(autoGenerate = true)val id: Int = 0,
    val stringId: String,
    val totalAnimals: String,
    val firstMilking: String,
    val secondMilking: String,
    val average: String,
    val totalPerDay: String,
    val date: String
)
