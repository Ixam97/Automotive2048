package com.ixam97.automotive2048.domain

/** Holds the direction and number of cells to move for each Tile on the Game Grid. */
data class TileMovements(
    val dir: SwipeDirection,
    val movements: Array<IntArray>
) {
    companion object {
        /** Returns empty TileMovements with Noop-Direction. */
        fun noopMovements(size: Int) = TileMovements(
            SwipeDirection.NOOP,
            Array(size) { IntArray(size) {0} }
        )
    }
}
