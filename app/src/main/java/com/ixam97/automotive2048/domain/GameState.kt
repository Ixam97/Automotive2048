package com.ixam97.automotive2048.domain

import android.util.Log

data class GameStateUpdate(
    val gameState: GameState,
    val tileMovements: TileMovements,
    val newTile: Tile?,
    val validMove: Boolean
)


data class GameState(
    val dimensions: Int,
    val gameStateHolder: Array<IntArray> = Array(dimensions) { IntArray(dimensions) {0} },
    val score: Int = 0,
    var isEmpty: Boolean = true
) {

    companion object {
        private data class RowCalcResult(
            val row: List<Int>,
            val rowMovements: IntArray,
            val score: Int
        )

        private data class GridCalcResult(
            val gameStateHolder: Array<IntArray>,
            val tileMovements: TileMovements,
            val score: Int
        )

        private fun calculateRowMovements(row: List<Int>): RowCalcResult {
            // Always assume a left swipe for this. Row is transformed beforehand.
            var scoreChange = 0
            val mutableRow = row.toMutableList()
            val mutableRowForMovements = row.toMutableList()

            val movementsArray = IntArray(row.size) {0}

            for (i in 0 until mutableRowForMovements.size - 1) {
                if (mutableRowForMovements[i] == 0) {
                    // if the tile is zero, just move all following tiles
                    for (j in i+1 until mutableRowForMovements.size) {
                        if (mutableRowForMovements[j] != 0) movementsArray[j] += 1
                    }
                } else {
                    // if not empty, merge if next is same, else no shift.
                    for (j in i+1 until mutableRowForMovements.size) {
                        if (mutableRowForMovements[i] == mutableRowForMovements[j]) {
                            for (k in j until mutableRowForMovements.size) {
                                if (mutableRowForMovements[k] != 0) movementsArray[k] += 1
                                mutableRowForMovements[j] = -1
                            }
                            break
                        } else if (mutableRowForMovements[j] != 0) {
                            break
                        }
                    }
                }
            }

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

            return RowCalcResult(mutableRow.toList(), movementsArray, scoreChange)
        }

        private fun rotateSquareIntArray(originalSquareIntArray: Array<IntArray>, reverse: Boolean): Array<IntArray> {
            val newGameStateHolder = Array(originalSquareIntArray.size) { IntArray(originalSquareIntArray.size) {0} }
            originalSquareIntArray.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { columnIndex, value ->
                    newGameStateHolder[columnIndex][rowIndex] = value
                }
            }
            return newGameStateHolder
        }

        private fun calcSwipeUp(gameStateHolder: Array<IntArray>): GridCalcResult {
            var score = 0
            val tmpGameStateHolder = rotateSquareIntArray(gameStateHolder, false)
            val tmpNewGameState = Array(gameStateHolder.size) { IntArray(gameStateHolder.size) {0} }
            val tmpTileMovements = Array(gameStateHolder.size) { IntArray(gameStateHolder.size) {0} }
            tmpGameStateHolder.forEachIndexed { index, row ->
                val result = calculateRowMovements(row.toList())
                tmpNewGameState[index] = result.row.toIntArray()
                tmpTileMovements[index] = result.rowMovements
                score += result.score
            }
            return GridCalcResult(
                rotateSquareIntArray(tmpNewGameState, true),
                TileMovements(SwipeDirection.UP, rotateSquareIntArray(tmpTileMovements, true)),
                score
            )
        }

        private fun calcSwipeDown(gameStateHolder: Array<IntArray>): GridCalcResult {
            var score = 0
            val tmpGameStateHolder = rotateSquareIntArray(gameStateHolder, false)
            val tmpNewGameState = Array(gameStateHolder.size) { IntArray(gameStateHolder.size) {0} }
            val tmpTileMovements = Array(gameStateHolder.size) { IntArray(gameStateHolder.size) {0} }
            tmpGameStateHolder.forEachIndexed { index, row ->
                val result = calculateRowMovements(row.reversedArray().toList())
                tmpNewGameState[index] = result.row.toIntArray().reversedArray()
                tmpTileMovements[index] = result.rowMovements.reversedArray()
                score += result.score
            }
            return GridCalcResult(
                rotateSquareIntArray(tmpNewGameState, true),
                TileMovements(SwipeDirection.DOWN, rotateSquareIntArray(tmpTileMovements, true)),
                score
            )
        }

        private fun  calcSwipeLeft(gameStateHolder: Array<IntArray>): GridCalcResult {
            var score = 0
            val tmpNewGameState = Array(gameStateHolder.size) { IntArray(gameStateHolder.size) {0} }
            val tmpTileMovements = Array(gameStateHolder.size) { IntArray(gameStateHolder.size) {0} }
            gameStateHolder.forEachIndexed { index, row ->
                val result = calculateRowMovements(row.toList())
                tmpNewGameState[index] = result.row.toIntArray()
                tmpTileMovements[index] = result.rowMovements
                score += result.score
            }
            return GridCalcResult(
                tmpNewGameState,
                TileMovements(SwipeDirection.LEFT, tmpTileMovements),
                score
            )
        }

        private fun calcSwipeRight(gameStateHolder: Array<IntArray>): GridCalcResult {
            var score = 0
            val tmpNewGameState = Array(gameStateHolder.size) { IntArray(gameStateHolder.size) {0} }
            val tmpTileMovements = Array(gameStateHolder.size) { IntArray(gameStateHolder.size) {0} }
            gameStateHolder.forEachIndexed { index, row ->
                val result = calculateRowMovements(row.reversedArray().toList())
                tmpNewGameState[index] = result.row.toIntArray().reversedArray()
                tmpTileMovements[index] = result.rowMovements.reversedArray()
                score += result.score
            }
            return GridCalcResult(
                tmpNewGameState,
                TileMovements(SwipeDirection.RIGHT, tmpTileMovements),
                score
            )
        }

        private fun Array<IntArray>.addTile(): Tile {
            val row = this.indices.random()
            val col = this.indices.random()
            if (this[row][col] != 0) {
                return this.addTile()
            } else {
                val newValue = if ((1..20).random() >= 18) 4 else 2
                this[row][col] = newValue
                return Tile(Cell(row, col), newValue)
            }
        }
    }

    fun getTileValue(row: Int, column: Int): Int? {
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
        val tileMovements: TileMovements
        var newGameStateHolder = Array(dimensions) { IntArray(dimensions) {0} }

        // perform the actual move
        val gridCalcResult =  when (direction) {
            SwipeDirection.UP -> calcSwipeUp(gameStateHolder)
            SwipeDirection.DOWN -> calcSwipeDown(gameStateHolder)
            SwipeDirection.LEFT -> calcSwipeLeft(gameStateHolder)
            SwipeDirection.RIGHT -> calcSwipeRight(gameStateHolder)
            SwipeDirection.NOOP -> GridCalcResult(gameStateHolder, TileMovements.noopMovements(gameStateHolder.size), 0)
        }

        Log.d("MOVEMENTS", "Direction: ${gridCalcResult.tileMovements.dir.name}")
        gridCalcResult.tileMovements.movements.forEachIndexed { index, row ->
            Log.d("MOVEMENTS", "Row ${index}: ${row.toList()}")
        }

        scoreChange += gridCalcResult.score
        tileMovements = gridCalcResult.tileMovements
        newGameStateHolder = gridCalcResult.gameStateHolder

        if (!newGameStateHolder.contentDeepEquals(gameStateHolder)) {
            val newTile = newGameStateHolder.addTile()
            return GameStateUpdate(this.copy(gameStateHolder = newGameStateHolder, score = this.score + scoreChange), tileMovements, newTile, true)
        }
        return GameStateUpdate(this, tileMovements, null, false)
        // return GameStateUpdate(GameState(dimensions, newGameState, score + scoreChange, isEmpty), tileMovements)
    }

    fun checkLostCondition(): Boolean {
        // check if the game was lost
        val nextUp = calcSwipeUp(gameStateHolder).gameStateHolder.contentDeepEquals(gameStateHolder)
        val nextDown = calcSwipeDown(gameStateHolder).gameStateHolder.contentDeepEquals(gameStateHolder)
        val nextLeft = calcSwipeLeft(gameStateHolder).gameStateHolder.contentDeepEquals(gameStateHolder)
        val nextRight = calcSwipeRight(gameStateHolder).gameStateHolder.contentDeepEquals(gameStateHolder)

        Log.i("STATE", "$nextUp && $nextDown && $nextLeft && $nextRight")

        return (nextUp && nextRight && nextLeft && nextDown)
    }

    fun checkWinCondition(): Boolean {
        gameStateHolder.forEach { row ->
            row.forEach { value ->
                if (value == 2048) {
                    return true
                }
            }
        }
        return false
    }

    fun initNewGame(dimensions: Int): GameState {
        val initArray = Array(dimensions) { IntArray(dimensions) {0} }

        initArray.addTile()
        initArray.addTile()

        return this.copy(score = 0, isEmpty = false, gameStateHolder = initArray)
    }

    inline fun forEachTile(action: (tile: Tile) -> Unit) {
        gameStateHolder.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, value ->
                action(Tile(Cell(rowIndex, columnIndex), value))
            }
        }
    }
}