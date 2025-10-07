package com.example.findpairgame.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameHistoryDao {
    @Query("SELECT * FROM game_history ORDER BY timestamp DESC")
    fun getAllGames(): Flow<List<GameHistoryEntity>>

    @Insert
    suspend fun insertGame(game: GameHistoryEntity)
}
