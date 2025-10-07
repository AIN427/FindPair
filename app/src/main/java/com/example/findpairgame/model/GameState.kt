package com.example.findpairgame.model

data class GameState(
    val cards: List<Card> = emptyList(),
    val pairsFound: Int = 0,
    val livesLeft: Int = 3,
    val isGameOver: Boolean = false,
    val isWon: Boolean = false,
    val firstFlippedCard: Card? = null,
    val secondFlippedCard: Card? = null,
    val isInitialReveal: Boolean = false
)
