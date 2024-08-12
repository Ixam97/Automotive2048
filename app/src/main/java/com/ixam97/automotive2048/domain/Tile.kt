package com.ixam97.automotive2048.domain

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Tile(
    // val id: Int,
    var cell: Cell,
    var value: Int,
    var visible: Boolean = true
) {

    fun applyTileMovements(tileMovements: TileMovements) {
        val movement = tileMovements.movements[cell.row][cell.col]
        if (movement != 0) {
            when (tileMovements.dir) {
                SwipeDirection.UP -> cell = cell.copy(row = cell.row - movement)
                SwipeDirection.DOWN -> cell = cell.copy(row = cell.row + movement)
                SwipeDirection.LEFT -> cell = cell.copy(col = cell.col - movement)
                SwipeDirection.RIGHT -> cell = cell.copy(col = cell.col + movement)
                SwipeDirection.NOOP -> { }
            }
        }
    }

    fun applyValue(newValue: Int) {
        value = newValue
    }
}
