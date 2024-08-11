package com.ixam97.automotive2048.domain

import android.util.Log

data class GameStateUpdate(
    val gameState: GameState,
    val tileMovements: List<TileMovement>
)


data class GameState(
    val dimensions: Int,
    private val gameStateHolder: Array<IntArray> = Array(dimensions) { IntArray(dimensions) {0} },
    val score: Int = 0,
    var isEmpty: Boolean = true
) {

    companion object {
        private data class RowCalcResult(
            val row: List<Int>,
            val tileMovements: List<TileMovement>,
            val score: Int
        )

        private fun calculateRowMovements(row: List<Int>): RowCalcResult {
            // Always assume a left swipe for this. Row is transformed beforehand.
            var scoreChange = 0
            val mutableRow = row.toMutableList()

            mutableRow.removeIf { it == 0 } // compress row

            for (i in 0 until mutableRow.size - 1) {
                if (mutableRow[i] == mutableRow[i + 1]) {
                    val value = mutableRow[i]
                    val newValue = value * 2
                    mutableRow[i] = newValue
                    mutableRow[i+1] = 0
                    scoreChange += newValue
                }
            }

            mutableRow.removeIf { it == 0 } // compress row again after additions

            for (i in mutableRow.size until row.size) {
                mutableRow.add(0)
            }

            return RowCalcResult(mutableRow.toList(), listOf(), scoreChange)
        }

        private fun rotateGameStateHolder(originalGameStateHolder: Array<IntArray>, reverse: Boolean): Array<IntArray> {
            val newGameStateHolder = Array(originalGameStateHolder.size) { IntArray(originalGameStateHolder.size) {0} }
            originalGameStateHolder.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { columnIndex, value ->
                    newGameStateHolder[columnIndex][rowIndex] = value
                }
            }
            return newGameStateHolder
        }

        private fun Array<IntArray>.addTile() {
            val row = this.indices.random()
            val col = this.indices.random()
            if (this[row][col] != 0) {
                this.addTile()
            } else {
                this[row][col] = if ((1..20).random() >= 18) 4 else 2
            }
        }
    }

    fun getTileValue(column: Int, row: Int): Int? {
        return if (column < dimensions && row < dimensions)
            gameStateHolder[row][column]
        else null
    }

    fun setTileValue(column:Int, row: Int, value: Int) {
        if (column < dimensions && row < dimensions)
            gameStateHolder[row][column] = value
    }

    fun makeMove(direction: SwipeDirection): GameStateUpdate {
        var scoreChange = 0
        val tileMovements: List<TileMovement> = listOf()
        var newGameState = Array(dimensions) { IntArray(dimensions) {0} }

        when (direction) {
            SwipeDirection.UP -> {
                val tmpGameStateHolder = rotateGameStateHolder(gameStateHolder, false)
                val tmpNewGameState = Array(dimensions) { IntArray(dimensions) {0} }
                tmpGameStateHolder.forEachIndexed { index, row ->
                    val result = calculateRowMovements(row.toList())
                    tmpNewGameState[index] = result.row.toIntArray()
                    scoreChange += result.score
                }
                newGameState = rotateGameStateHolder(tmpNewGameState, true)
            }
            SwipeDirection.DOWN -> {
                val tmpGameStateHolder = rotateGameStateHolder(gameStateHolder, false)
                val tmpNewGameState = Array(dimensions) { IntArray(dimensions) {0} }
                tmpGameStateHolder.forEachIndexed { index, row ->
                    val result = calculateRowMovements(row.reversedArray().toList())
                    tmpNewGameState[index] = result.row.toIntArray().reversedArray()
                    scoreChange += result.score
                }
                newGameState = rotateGameStateHolder(tmpNewGameState, true)

            }
            SwipeDirection.LEFT -> {
                gameStateHolder.forEachIndexed { index, row ->
                    val result = calculateRowMovements(row.toList())
                    newGameState[index] = result.row.toIntArray()
                    scoreChange += result.score
                }
            }
            SwipeDirection.RIGHT -> {
                gameStateHolder.forEachIndexed { index, row ->
                    val result = calculateRowMovements(row.reversedArray().toList())
                    newGameState[index] = result.row.toIntArray().reversedArray()
                    scoreChange += result.score
                }
            }
        }
        newGameState.addTile()
        return GameStateUpdate(this.copy(gameStateHolder = newGameState, score = this.score + scoreChange), tileMovements)
        // return GameStateUpdate(GameState(dimensions, newGameState, score + scoreChange, isEmpty), tileMovements)
    }

    fun initNewGame(dimensions: Int): GameState {
        val initArray = Array(dimensions) { IntArray(dimensions) {0} }

        initArray.addTile()
        initArray.addTile()

        return this.copy(score = 0, isEmpty = false, gameStateHolder = initArray)
    }
}