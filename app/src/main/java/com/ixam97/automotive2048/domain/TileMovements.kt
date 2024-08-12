package com.ixam97.automotive2048.domain

data class TileMovements(
    val dir: SwipeDirection,
    val movements: Array<IntArray>
) {
    companion object {
        fun noopMovements(size: Int) = TileMovements(
            SwipeDirection.NOOP,
            Array(size) { IntArray(size) {0} }
        )
    }
}
