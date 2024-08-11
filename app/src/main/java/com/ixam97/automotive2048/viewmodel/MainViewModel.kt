package com.ixam97.automotive2048.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.ixam97.automotive2048.domain.GameState
import com.ixam97.automotive2048.domain.SwipeDirection
import com.ixam97.automotive2048.repository.GameRepository

class MainViewModel(private val gameRepository: GameRepository) : ViewModel() {
    private val TAG = "MainViewModel"

    private val gameStateHistory = mutableListOf<GameState>()

    // var score by mutableIntStateOf(0)
        // private set
    var highscore by mutableIntStateOf(0)
        private set
    var currentScreenIndex by mutableIntStateOf(0)
        private set
    var gameState by mutableStateOf(GameState(4))
        private set
    var canUndo by mutableStateOf(false)
        private set
    var gameWon by mutableStateOf(false)
        private set
    var gameWinDismissed by mutableStateOf(false)
        private set
    var gameLost by mutableStateOf(false)
        private set

    init {
        gameState = gameRepository.getSavedGameState()
        highscore = gameRepository.getHighScore()
        gameStateHistory.addAll(gameRepository.getGameHistory())
        gameWinDismissed = gameRepository.getWinDismissed()
    }

    fun historySize() = gameStateHistory.size

    fun settingsClicked() {
        Log.i(TAG, "Open Settings")
        currentScreenIndex = 1
    }

    fun settingsBackClicked() {
        Log.i(TAG, "Close Settings")
        currentScreenIndex = 0
    }

    fun swiped(dir: SwipeDirection) {
        Log.i(TAG, "Swiped: ${dir.name}")

        gameState.makeMove(dir).let {
            if (it.validMove) {
                gameStateHistory.add(gameState)
                if(gameStateHistory.size > 5) {
                    gameStateHistory.removeAt(0)
                }
                gameState = it.gameState
                if (gameState.score > highscore) {
                    highscore = gameState.score
                }
                gameRepository.save(
                    gameState = gameState,
                    highscore =  highscore,
                    winDismissed = gameWinDismissed,
                    gameHistory = gameStateHistory
                )
            } else {
                Log.i("GAME CONDITION", "INVALID MOVE")
            }

        }

        if (gameState.checkWinCondition()) {
            Log.e("GAME CONDITION", "GAME WON!")
            gameWon = true
        } else if (gameState.checkLostCondition()) {
            Log.e("GAME CONDITION", "GAME LOST!")
            gameLost = true
        }

        canUndo = gameStateHistory.size > 0
    }

    fun testFunction() {
        Log.w(TAG, "Test Function")
    }

    fun restartGame() {
        Log.i(TAG, "Game restart requested")
        gameState = gameState.initNewGame(4)
        gameStateHistory.clear()
        gameRepository.save(gameState = gameState, gameHistory = gameStateHistory, winDismissed = gameWinDismissed)
        canUndo = gameStateHistory.size > 0
    }

    fun undoMove() {
        if (gameStateHistory.size > 0) {
            gameState = gameStateHistory.last()
            gameStateHistory.removeAt(gameStateHistory.lastIndex)
        }
        gameRepository.save(gameState = gameState, gameHistory = gameStateHistory, winDismissed = gameWinDismissed)
        canUndo = gameStateHistory.size > 0
    }
}