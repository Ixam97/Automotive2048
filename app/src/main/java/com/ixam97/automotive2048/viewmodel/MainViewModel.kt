package com.ixam97.automotive2048.viewmodel

import android.app.ActivityManager
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ixam97.automotive2048.domain.SwipeDirection
import com.ixam97.automotive2048.repository.GameRepository
import com.ixam97.automotive2048.domain.GameGridTiles
import com.ixam97.automotive2048.domain.GameState
import com.ixam97.automotive2048.ui.fadeInAnimationDelay
import com.ixam97.automotive2048.ui.fadeInAnimationDuration
import com.ixam97.automotive2048.ui.swipeAnimationDuration
import com.ixam97.automotive2048.utils.fetchCpuName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.max

class MainViewModel(private val gameRepository: GameRepository) : ViewModel() {

    private val gameStateHistory = gameRepository.getGameHistory().toMutableStateList()

    /** While the Tiles are moving or new Tiles are added, no swiping interaction should be possible. */
    private var blockSwiping = false

    private var versionClickCounter = 0

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
    var link by mutableIntStateOf(0)
        private set
    var gameGridTiles by mutableStateOf(GameGridTiles(gameState))
        private set

    var sysInfoString by mutableStateOf("")
        private set

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
    fun openLinkViewer(id: Int) {
        when (id) {
            1, 2 -> link = id
        }
    }
    fun versionClick(context: Context) {
        if (versionClickCounter < 7) {
            versionClickCounter++
        } else {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                    val memInfo = ActivityManager.MemoryInfo()
                    activityManager.getMemoryInfo(memInfo)

                    sysInfoString = "\nCPU-Info: ${fetchCpuName()}\nRAM-Info:\n      total: ${memInfo.totalMem / 1_000_000} MB\n      available: ${memInfo.availMem / 1_000_000} MB"
                }
            }
        }
    }
    fun closeLinkViewer() {
        link = 0
    }
    fun openHowToPlayDialog() {
        showHowToPlayDialog = true
    }

    fun swiped(dir: SwipeDirection) {
        if (blockSwiping) return

        gameState.makeMove(dir).let { gameStateUpdate ->
            if (gameStateUpdate.validMove) {
                blockSwiping = true

                /** Add current GameState to Undo-History and delete entries > 5. */
                gameStateHistory.add(gameState)
                if (gameStateHistory.size > 5) { gameStateHistory.removeAt(0) }

                gameState = gameStateUpdate.gameState
                if (gameState.score > highscore) { highscore = gameState.score }

                /** Save the new Game State. */
                gameRepository.saveGame(gameState, gameStateHistory, gameWinDismissed, highscore)

                gameGridTiles.let {
                    it.applyMovements(gameStateUpdate.tileMovements)
                    it.updateValues(gameState)
                    it.addNewTile(gameState.addRandomTile())
                }

                /**
                 * Deleting single tiles from the GameGridState would cause wierd animations in
                 * the compose UI. Therefore gameGridTiles is initialized again after each move.
                 */
                viewModelScope.launch {
                    delay(max(swipeAnimationDuration, (fadeInAnimationDelay + fadeInAnimationDuration)) + 50L)

                    gameGridTiles = GameGridTiles(gameState)
                    blockSwiping = false

                    if (gameState.checkWinCondition() && !gameWinDismissed) { gameWon = true }
                    else if (gameState.checkLostCondition()) { gameLost = true }
                }
            }
        }
    }

    private fun saveSettings() {
        gameRepository.saveSettings(undoButtonEnabled, oemSchemeEnabled)
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
        gameState = GameState.initNewGame(dimensions)
        gameStateHistory.clear()
        gameLost = false
        gameWon = false
        gameWinDismissed = false
        gameGridTiles = GameGridTiles(gameState)
        gameRepository.saveGame(gameState, gameStateHistory, gameWinDismissed)
    }

    fun undoMove() {
        if (gameStateHistory.size > 0) {
            gameState = gameStateHistory.last()
            gameGridTiles = GameGridTiles(gameState)
            gameStateHistory.removeAt(gameStateHistory.lastIndex)
            gameLost = false
        }
        gameRepository.saveGame(gameState, gameStateHistory, gameWinDismissed)
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