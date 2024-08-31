package com.ixam97.automotive2048.domain

data class GameStateUpdate(
    val gameState: GameState,
    val tileMovements: TileMovements,
    val validMove: Boolean
)