package com.ixam97.automotive2048.repository

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ixam97.automotive2048.domain.GameState

private const val KEY_SHARED_PREFS = "com.ixam97.automotive2048_shared_prefs"
private const val KEY_HIGHSCORE = "key_highscore"
private const val KEY_GAME_STATE = "key_game_state"
private const val KEY_GAME_HISTORY = "key_game_history"
private const val KEY_WIN_DISMISSED = "key_win_dismissed"
private const val KEY_ALLOW_UNDO = "key_allow_undo"


class GameRepository(context: Context) {

    private val sharedPrefs = context.getSharedPreferences(KEY_SHARED_PREFS, Context.MODE_PRIVATE)

    fun getSavedGameState(): GameState {
        val gson = Gson()
        val gameStateString = sharedPrefs.getString(KEY_GAME_STATE, gson.toJson(GameState(4).initNewGame(4)))
        return gson.fromJson(gameStateString, GameState::class.java)
    }

    fun getHighScore(): Int {
        return sharedPrefs.getInt(KEY_HIGHSCORE, 0)
    }

    fun getWinDismissed(): Boolean {
        return sharedPrefs.getBoolean(KEY_WIN_DISMISSED, false)
    }

    fun getGameHistory(): List<GameState> {
        val gson = Gson()
        val gameHistoryString = sharedPrefs.getString(KEY_GAME_HISTORY, gson.toJson(listOf<GameState>()))
        return gson.fromJson(gameHistoryString, object: TypeToken<List<GameState>>() {}.type)
    }

    fun getAllowUndo(): Boolean {
        return sharedPrefs.getBoolean(KEY_ALLOW_UNDO, true)
    }

    fun saveSettings(
        allowUndo: Boolean
    ) {
        sharedPrefs.edit()
            .putBoolean(KEY_ALLOW_UNDO, allowUndo)
            .apply()
    }

    fun saveGame(
        gameState: GameState,
        gameHistory: List<GameState>,
        winDismissed: Boolean,
        highscore: Int = sharedPrefs.getInt(KEY_HIGHSCORE, 0)
    ) {

        val gson = Gson()
        val gameStateString = gson.toJson(gameState)
        val gameHistoryString = gson.toJson(gameHistory)

        sharedPrefs.edit().apply {
            putInt(KEY_HIGHSCORE, highscore)
            putString(KEY_GAME_STATE, gameStateString)
            putString(KEY_GAME_HISTORY, gameHistoryString)
            putBoolean(KEY_WIN_DISMISSED, winDismissed)
        }.apply()

    }
}