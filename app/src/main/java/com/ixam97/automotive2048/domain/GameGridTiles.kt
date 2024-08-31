package com.ixam97.automotive2048.domain

/**
 * Contains and modifies the Tiles visible on the Game Grid.
 */
class GameGridTiles(gameState: GameState) {

    /** List of currently visible Tiles on the Game Grid. */
    var tiles = mutableListOf<Tile>()
        private set

    init {
        tiles.clear()
        gameState.forEachCell { cell, value ->
            if (value != 0) {
                tiles.add(Tile(cell, value))
            }
        }
    }

    /**
     * Update the values of all Tiles to the corresponding GameState values. Does not add new tiles.
     */
    fun updateValues(newGameState: GameState) {
        tiles.forEach { tile ->
            tile.value = newGameState.getTileValue(tile.cell.row, tile.cell.col)?: 0
        }
    }

    /**
     * Adds a new Tile to the Tiles list. The Tile's visibility will be set to false to make the
     * Tile fade into existence on the Game Grid.
     */
    fun addNewTile(tile: Tile) {
        tiles.add(tile.copy(visible = false))
    }

    /**
     * Applies the TileMovements to the list of Tiles. By this, the Cell of each moved Tile will
     * update and animations will be set to true for the corresponding direction on those Tiles
     * only.
     */
    fun applyMovements(tileMovements: TileMovements) {
        tiles.forEach {
            it.applyTileMovements(tileMovements)
        }
    }
}