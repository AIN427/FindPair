package com.example.findpairgame.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_history")
data class GameHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pairsFound: Int,
    val isWon: Boolean,
    val timestamp: Long
)
