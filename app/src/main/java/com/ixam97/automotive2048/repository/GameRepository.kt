package com.ixam97.automotive2048.repository

import android.content.Context
import com.google.gson.Gson
import com.ixam97.automotive2048.domain.GameState

private const val KEY_SHARED_PREFS = "com.ixam97.automotive2048_shared_prefs"
private const val KEY_HIGHSCORE = "key_highscore"
private const val KEY_GAME_STATE = "key_game_state"

class GameRepository(context: Context) {

    private val sharedPrefs = context.getSharedPreferences(KEY_SHARED_PREFS, Context.MODE_PRIVATE)

    fun getSavedGameState(): GameState {
        val gson = Gson()
        val gameStateString = sharedPrefs.getString(KEY_GAME_STATE, gson.toJson(GameState(4).initNewGame(4)))
        return gson.fromJson(gameStateString, GameState::class.java)
    }

    fun save(gameState: GameState, highscore: Int = sharedPrefs.getInt(KEY_HIGHSCORE, 0)) {

        val gameStateString = Gson().toJson(gameState)

        sharedPrefs.edit().apply {
            putInt(KEY_HIGHSCORE, highscore)
            putString(KEY_GAME_STATE, gameStateString)
        }.apply()

    }

    fun getHighScore(): Int {
        return sharedPrefs.getInt(KEY_HIGHSCORE, 0)
    }
}