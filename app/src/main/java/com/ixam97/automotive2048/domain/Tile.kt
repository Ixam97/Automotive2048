package com.ixam97.automotive2048.domain

/** Contains all information necessary to show and animate a Tile on the Game Grid. */
data class Tile(
    var cell: Cell,
    var value: Int,
    var visible: Boolean = true,
    var animateVertical: Boolean = false,
    var animateHorizontal: Boolean = false
) {

    /** Apply TileMovements to the cell if applicable. */
    fun applyTileMovements(tileMovements: TileMovements) {
        val movement = tileMovements.movements[cell.row][cell.col]
        if (movement != 0) {
            when (tileMovements.dir) {
                SwipeDirection.UP -> {
                    cell = cell.copy(row = cell.row - movement)
                    animateVertical = true
                    animateHorizontal = false
                }
                SwipeDirection.DOWN -> {
                    cell = cell.copy(row = cell.row + movement)
                    animateVertical = true
                    animateHorizontal = false
                }
                SwipeDirection.LEFT -> {
                    cell = cell.copy(col = cell.col - movement)
                    animateHorizontal = true
                    animateVertical = false
                }
                SwipeDirection.RIGHT -> {
                    cell = cell.copy(col = cell.col + movement)
                    animateHorizontal = true
                    animateVertical = false
                }
                SwipeDirection.NOOP -> {
                    animateHorizontal = false
                    animateVertical = false
                }
            }
        }
    }
}
