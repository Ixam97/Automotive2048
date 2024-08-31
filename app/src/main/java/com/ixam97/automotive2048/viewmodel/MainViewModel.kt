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
import com.ixam97.automotive2048.domain.GameGridTiles
import com.ixam97.automotive2048.domain.GameState
import com.ixam97.automotive2048.ui.fadeInAnimationDelay
import com.ixam97.automotive2048.ui.fadeInAnimationDuration
import com.ixam97.automotive2048.ui.swipeAnimationDuration
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.max

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

    var gameGridTiles by mutableStateOf(GameGridTiles(gameState))

    init {
        viewModelScope.launch {
            delay(100)
            blockSwiping = false
        }
    }

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

    fun swiped(dir: SwipeDirection) {
        if (blockSwiping) return
        Log.i(TAG, "Swiped: ${dir.name}")

        gameState.makeMove(dir).let { gameStateUpdate ->
            if (gameStateUpdate.validMove) {
                blockSwiping = true

                tileMovements = gameStateUpdate.tileMovements
                gameGridTiles.applyMovements(tileMovements)

                gameStateHistory.add(gameState)
                if (gameStateHistory.size > 5) { gameStateHistory.removeAt(0) }

                gameState = gameStateUpdate.gameState
                gameGridTiles.updateValues(gameState)
                gameGridTiles.addNewTile(gameState.addRandomTile())
                tileMovements = TileMovements.noopMovements(gameState.dimensions)

                if (gameState.score > highscore) { highscore = gameState.score }

                gameRepository.saveGame(
                    gameState = gameState,
                    highscore =  highscore,
                    winDismissed = gameWinDismissed,
                    gameHistory = gameStateHistory
                )

                viewModelScope.launch {
                    delay(max(swipeAnimationDuration, fadeInAnimationDelay + fadeInAnimationDuration) + 50L)

                    /**
                     * Deleting single tiles from the GameGridState would cause wierd animations in
                     * the compose UI. Therefore gameGridState is initialized again after each move.
                     */
                    gameGridTiles = GameGridTiles(gameState)
                    blockSwiping = false

                    if (gameState.checkWinCondition() && !gameWinDismissed) {
                        Log.e("GAME CONDITION", "GAME WON!")
                        gameWon = true
                    } else if (gameState.checkLostCondition()) {
                        Log.e("GAME CONDITION", "GAME LOST!")
                        gameLost = true
                    }
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

    fun restartGame(dimensions: Int = 4) {
        Log.i(TAG, "Game restart requested")
        blockSwiping = true
        viewModelScope.launch {
            gameState = GameState.initNewGame(dimensions)
            gameStateHistory.clear()
            gameLost = false
            gameWon = false
            gameWinDismissed = false
            gameGridTiles = GameGridTiles(gameState)
            gameRepository.saveGame(gameState = gameState, gameHistory = gameStateHistory, winDismissed = gameWinDismissed)

            delay(100)
            blockSwiping = false
        }
    }

    fun undoMove() {
        blockSwiping = true
        viewModelScope.launch {
            if (gameStateHistory.size > 0) {
                gameState = gameStateHistory.last()
                gameGridTiles = GameGridTiles(gameState)
                gameStateHistory.removeAt(gameStateHistory.lastIndex)
                gameLost = false
            }
            gameRepository.saveGame(
                gameState = gameState,
                gameHistory = gameStateHistory,
                winDismissed = gameWinDismissed
            )
            delay(100)
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