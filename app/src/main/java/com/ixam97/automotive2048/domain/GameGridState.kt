package com.ixam97.automotive2048.domain

class GameGridState(private var gameState: GameState) {

    val tiles = mutableListOf<Tile>()
    private var tileOccupation = Array(gameState.dimensions) { Array(gameState.dimensions) { false } }

    private fun clearedTileOccupation() = Array(gameState.dimensions) { Array(gameState.dimensions) { false } }

    var animate = true
        private set

    init {
        resetGameState(gameState)
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
        val numTiles = tiles.size
        for (i in 0 until numTiles) {
            tiles[i].applyTileMovements(tileMovements)
            tileOccupation = clearedTileOccupation()
            if (tileOccupation[tiles[i].cell.row][tiles[i].cell.col]) {
                tiles.removeAt(i)
            } else {
                tileOccupation[tiles[i].cell.row][tiles[i].cell.col] = true
            }
        }
    }

    fun resetGameState(newGameState: GameState) {
        animate = false
        tiles.clear()
        newGameState.forEachTile { tile ->
            if (tile.value != 0) {
                tiles.add(tile)
                tileOccupation[tile.cell.row][tile.cell.col] = true
            }
        }

        gameState = newGameState
    }

    fun enableAnimations() {
        animate = true
    }

    fun disableAnimations() {
        animate = false
    }

    fun updateVisibility() {
        tiles.forEach { it.visible = true }
    }
}