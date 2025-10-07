package com.example.findpairgame.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.findpairgame.R
import com.example.findpairgame.data.AppDatabase
import com.example.findpairgame.data.GameHistoryEntity
import com.example.findpairgame.model.Card
import com.example.findpairgame.model.GameState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val gameHistoryDao = database.gameHistoryDao()

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _vibrationEvent = MutableStateFlow(0L)
    val vibrationEvent: StateFlow<Long> = _vibrationEvent.asStateFlow()

    private val emojis = listOf(
        "🐶", "🐱", "🐭", "🐹", "🐰", "🦊"
    )

    init {
        initGame()
    }

    fun initGame() {
        val cards = mutableListOf<Card>()
        emojis.forEachIndexed { index, emoji ->
            cards.add(Card(id = index * 2, pairId = index, emoji = emoji))
            cards.add(Card(id = index * 2 + 1, pairId = index, emoji = emoji))
        }
        cards.shuffle()

        val flippedCards = cards.map { it.copy(isFlipped = true) }

        _gameState.value = GameState(
            cards = flippedCards,
            pairsFound = 0,
            livesLeft = 3,
            isGameOver = false,
            isWon = false,
            isInitialReveal = true
        )

        viewModelScope.launch {
            delay(2000)
            val hiddenCards = _gameState.value.cards.map { it.copy(isFlipped = false) }
            _gameState.value = _gameState.value.copy(
                cards = hiddenCards,
                isInitialReveal = false
            )
        }
    }

    fun onCardClick(card: Card) {
        val state = _gameState.value
        if (state.isGameOver || card.isMatched || card.isFlipped || state.isInitialReveal) return

        val updatedCards = state.cards.map {
            if (it.id == card.id) it.copy(isFlipped = true) else it
        }

        when {
            state.firstFlippedCard == null -> {
                _gameState.value = state.copy(
                    cards = updatedCards,
                    firstFlippedCard = card
                )
            }
            state.secondFlippedCard == null -> {
                _gameState.value = state.copy(
                    cards = updatedCards,
                    secondFlippedCard = card
                )
                checkMatch()
            }
        }
    }

    private fun checkMatch() {
        viewModelScope.launch {
            delay(1000)
            val state = _gameState.value
            val first = state.firstFlippedCard
            val second = state.secondFlippedCard

            if (first != null && second != null) {
                if (first.pairId == second.pairId) {
                    val updatedCards = state.cards.map {
                        if (it.id == first.id || it.id == second.id) {
                            it.copy(isMatched = true)
                        } else it
                    }
                    val newPairsFound = state.pairsFound + 1
                    val isWon = newPairsFound == 6

                    _gameState.value = state.copy(
                        cards = updatedCards,
                        pairsFound = newPairsFound,
                        firstFlippedCard = null,
                        secondFlippedCard = null,
                        isWon = isWon,
                        isGameOver = isWon
                    )

                    if (isWon) {
                        saveGameResult(newPairsFound, true)
                    }
                } else {
                    _vibrationEvent.value = System.currentTimeMillis()

                    val updatedCards = state.cards.map {
                        if (it.id == first.id || it.id == second.id) {
                            it.copy(isFlipped = false)
                        } else it
                    }
                    val newLives = state.livesLeft - 1
                    val isLost = newLives <= 0

                    _gameState.value = state.copy(
                        cards = updatedCards,
                        livesLeft = newLives,
                        firstFlippedCard = null,
                        secondFlippedCard = null,
                        isGameOver = isLost,
                        isWon = false
                    )

                    if (isLost) {
                        saveGameResult(state.pairsFound, false)
                    }
                }
            }
        }
    }

    private fun saveGameResult(pairsFound: Int, isWon: Boolean) {
        viewModelScope.launch {
            gameHistoryDao.insertGame(
                GameHistoryEntity(
                    pairsFound = pairsFound,
                    isWon = isWon,
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }
}
