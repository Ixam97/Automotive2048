package com.ixam97.automotive2048.domain

class GameGridState(private var gameState: GameState) {

    var counter: Long = 0L

    val tiles = mutableListOf<Tile>()

    var animate = false
        private set

    init {
        initGameState(gameState)
    }

    fun updateValues(newGameState: GameState) {
        counter++
        animate = true
        tiles.forEach { tile ->
            tile.value = newGameState.getTileValue(tile.cell.row, tile.cell.col)?: 0
        }
    }

    fun addNewTile(tile: Tile) {
        counter++
        tiles.add(tile.copy(visible = false))
    }

    fun applyMovements(tileMovements: TileMovements) {
        counter++
        tiles.forEach {
            it.applyTileMovements(tileMovements)
        }
    }

    private fun initGameState(newGameState: GameState) {
        counter++
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
        counter++
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