package com.example.findpairgame.model

data class Card(
    val id: Int,
    val pairId: Int,
    val emoji: String,
    val isFlipped: Boolean = false,
    val isMatched: Boolean = false
)
