package com.ixam97.automotive2048.domain

class GameGridState(private var gameState: GameState) {

    val tiles = arrayListOf<Tile>()

    var animate = false
        private set

    init {
        initGameState(gameState)
    }

    fun updateGameState(newGameState: GameState, newTile: Tile?) {
        animate = true
        tiles.forEach { tile ->
            tile.value = newGameState.getTileValue(tile.cell.row, tile.cell.col)?: 0
        }

        newTile?.let {
            tiles.add(it.copy(visible = false))
        }
    }

    fun applyMovements(tileMovements: TileMovements) {
        tiles.forEach {
            it.applyTileMovements(tileMovements)
        }
    }

    private fun initGameState(newGameState: GameState) {
        animate = false
        tiles.clear()
        newGameState.forEachTile { tile ->
            if (tile.value != 0) {
                tiles.add(tile)
            }
        }

        gameState = newGameState
    }

    fun enableAnimations() {
        animate = true
    }
/*
    fun disableAnimations() {
        animate = false
    }

    fun updateVisibility() {
        tiles.forEach { it.visible = true }
    }
 */
}