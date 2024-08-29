package com.ixam97.automotive2048.domain

data class Tile(
    // val id: Int = idCounter,
    var cell: Cell,
    var value: Int,
    var visible: Boolean = true,
    var animateVertical: Boolean = false,
    var animateHorizontal: Boolean = false
) {
    /*
    init {
        idCounter++
    }

    companion object {
        var idCounter: Int = 0
    }
     */

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
