package com.ixam97.automotive2048.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ixam97.automotive2048.domain.SwipeDirection
import com.ixam97.automotive2048.domain.TileMovements
import com.ixam97.automotive2048.repository.GameRepository
import com.ixam97.automotive2048.domain.GameGridState
import com.ixam97.automotive2048.ui.fadeInAnimationDelay
import com.ixam97.automotive2048.ui.fadeInAnimationDuration
import com.ixam97.automotive2048.ui.swipeAnimationDuration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(private val gameRepository: GameRepository) : ViewModel() {
    private val TAG = "MainViewModel"

    private val gameStateHistory = gameRepository.getGameHistory().toMutableStateList()

    private var blockSwiping = true

    var highscore by mutableIntStateOf(gameRepository.getHighScore())
        private set
    var gameState by mutableStateOf(gameRepository.getSavedGameState())
        private set
    var gameWinDismissed by mutableStateOf(gameRepository.getWinDismissed())
        private set
    var undoButtonEnabled by mutableStateOf(gameRepository.getAllowUndo())
        private set
    var oemSchemeEnabled by mutableStateOf(gameRepository.getOemSchemeEnabled())
        private set

    var currentScreenIndex by mutableIntStateOf(0)
        private set
    var gameWon by mutableStateOf(false)
        private set
    var gameLost by mutableStateOf(false)
        private set
    var showRestartDialog by mutableStateOf(false)
        private set
    var showHowToPlayDialog by mutableStateOf(gameRepository.getFirstLaunch())
        private set

    var tileMovements by mutableStateOf(TileMovements.noopMovements(gameState.dimensions))
        private set

    var gameGridState by mutableStateOf(GameGridState(gameState))

    fun historySize() = gameStateHistory.size

    fun settingsClicked() {
        currentScreenIndex = 1
    }
    fun licensesClicked() {
        currentScreenIndex = 2
    }
    fun settingsBackClicked() {
        currentScreenIndex = 0
    }
    fun licensesBackClicked() {
        currentScreenIndex = 1
    }
    fun closeHowToPlayDialog() {
        gameRepository.setFirstLaunch(false)
        showHowToPlayDialog = false
    }
    fun openHowToPlayDialog() {
        showHowToPlayDialog = true
    }

    init {
        viewModelScope.launch {
            delay(100)
            gameGridState.enableAnimations()
            blockSwiping = false
        }
    }

    fun swiped(dir: SwipeDirection) {
        if (blockSwiping) return
        Log.i(TAG, "Swiped: ${dir.name}")

        gameState.makeMove(dir).let { gameStateUpdate ->
            if (gameStateUpdate.validMove) {
                blockSwiping = true

                tileMovements = gameStateUpdate.tileMovements
                val newGameState = gameStateUpdate.gameState

                gameGridState.applyMovements(tileMovements)

                gameStateHistory.add(gameState)
                if(gameStateHistory.size > 5) {
                    gameStateHistory.removeAt(0)
                }

                gameState = newGameState
                if (gameState.score > highscore) {
                    highscore = gameState.score
                }
                gameRepository.saveGame(
                    gameState = gameState,
                    highscore =  highscore,
                    winDismissed = gameWinDismissed,
                    gameHistory = gameStateHistory
                )

                if (gameState.checkWinCondition() && !gameWinDismissed) {
                    Log.e("GAME CONDITION", "GAME WON!")
                    gameWon = true
                } else if (gameState.checkLostCondition()) {
                    Log.e("GAME CONDITION", "GAME LOST!")
                    gameLost = true
                }
                gameGridState.updateValues(gameState)
                gameGridState.addNewTile(gameState.addRandomTile())
                tileMovements = TileMovements.noopMovements(gameState.dimensions)

                viewModelScope.launch {
                    delay(swipeAnimationDuration + fadeInAnimationDelay + fadeInAnimationDuration + 20L)
                    gameGridState = GameGridState(gameState)
                    blockSwiping = false
                }
            } else {
                Log.i("GAME CONDITION", "INVALID MOVE")
            }

        }
    }

    private fun saveSettings() {
        gameRepository.saveSettings(
            allowUndo = undoButtonEnabled,
            oemSchemeEnabled = oemSchemeEnabled
        )
    }

    fun toggleUndoButtonSetting() {
        undoButtonEnabled = !undoButtonEnabled
        saveSettings()
    }

    fun toggleOemSchemeSetting() {
        oemSchemeEnabled = !oemSchemeEnabled
        saveSettings()
    }

    fun restartGame() {
        Log.i(TAG, "Game restart requested")
        blockSwiping = true
        viewModelScope.launch {
            gameState = gameState.initNewGame(4)
            gameStateHistory.clear()
            gameLost = false
            gameWon = false
            gameWinDismissed = false
            gameGridState = GameGridState(gameState)
            gameRepository.saveGame(gameState = gameState, gameHistory = gameStateHistory, winDismissed = gameWinDismissed)

            delay(100)
            gameGridState.enableAnimations()
            blockSwiping = false
        }
    }

    fun undoMove() {
        blockSwiping = true
        viewModelScope.launch {
            if (gameStateHistory.size > 0) {
                gameState = gameStateHistory.last()
                gameGridState = GameGridState(gameState)
                gameStateHistory.removeAt(gameStateHistory.lastIndex)
                gameLost = false
            }
            gameRepository.saveGame(
                gameState = gameState,
                gameHistory = gameStateHistory,
                winDismissed = gameWinDismissed
            )
            delay(100)
            gameGridState.enableAnimations()
            blockSwiping = false
        }
    }

    fun dismissWin() {
        gameWinDismissed = true
    }

    fun closeRestartDialog() {
        showRestartDialog = false
    }

    fun showRestartDialog() {
        showRestartDialog = true
    }
}