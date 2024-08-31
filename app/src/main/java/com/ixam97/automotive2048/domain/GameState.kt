package com.ixam97.automotive2048.domain

/**
 * The current state of a running Game. Contains the dimensions of the Game Grid, the Cell
 * occupation and values and the score. Member functions are used to calculate Game interaction
 * based on the current state of the game.
 */
data class GameState(
    val dimensions: Int,
    val gameStateHolder: Array<IntArray> = Array(dimensions) { IntArray(dimensions) {0} },
    val score: Int = 0
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

        /**
         * This calculates new Row values, assuming a left swipe as input. rowMovements contains Int
         * values of how many Cells a Tile is moved to the left. Inputs and results have to be
         * transformed according to the swipe direction.
         */
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

            return RowCalcResult(
                row = mutableRow.toList(),
                rowMovements =  movementsArray,
                score = scoreChange
            )
        }

        /** Rotates a Game Grid by 90° to calculate vertical Row Movements. */
        private fun rotateSquareIntArray(originalSquareIntArray: Array<IntArray>): Array<IntArray> {
            val newGameStateHolder = Array(originalSquareIntArray.size) { IntArray(originalSquareIntArray.size) {0} }
            originalSquareIntArray.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { columnIndex, value ->
                    newGameStateHolder[columnIndex][rowIndex] = value
                }
            }
            return newGameStateHolder
        }

        /** Calculates a new Game Grind state für an Up-Swipe. Game Grid gets Rotated for this. */
        private fun calcSwipeUp(gameStateHolder: Array<IntArray>): GridCalcResult {
            var score = 0
            val tmpGameStateHolder = rotateSquareIntArray(gameStateHolder)
            val tmpNewGameState = Array(gameStateHolder.size) { IntArray(gameStateHolder.size) {0} }
            val tmpTileMovements = Array(gameStateHolder.size) { IntArray(gameStateHolder.size) {0} }
            tmpGameStateHolder.forEachIndexed { index, row ->
                val result = calculateRowMovements(row.toList())
                tmpNewGameState[index] = result.row.toIntArray()
                tmpTileMovements[index] = result.rowMovements
                score += result.score
            }
            return GridCalcResult(
                rotateSquareIntArray(tmpNewGameState),
                TileMovements(SwipeDirection.UP, rotateSquareIntArray(tmpTileMovements)),
                score
            )
        }

        /** Calculates a new Game Grind state für an Down-Swipe. Game Grid gets Rotated and reversed for this. */
        private fun calcSwipeDown(gameStateHolder: Array<IntArray>): GridCalcResult {
            var score = 0
            val tmpGameStateHolder = rotateSquareIntArray(gameStateHolder)
            val tmpNewGameState = Array(gameStateHolder.size) { IntArray(gameStateHolder.size) {0} }
            val tmpTileMovements = Array(gameStateHolder.size) { IntArray(gameStateHolder.size) {0} }
            tmpGameStateHolder.forEachIndexed { index, row ->
                val result = calculateRowMovements(row.reversedArray().toList())
                tmpNewGameState[index] = result.row.toIntArray().reversedArray()
                tmpTileMovements[index] = result.rowMovements.reversedArray()
                score += result.score
            }
            return GridCalcResult(
                rotateSquareIntArray(tmpNewGameState),
                TileMovements(SwipeDirection.DOWN, rotateSquareIntArray(tmpTileMovements)),
                score
            )
        }

        /** Calculates a new Game Grind state für an Left-Swipe. No transformations needed. */
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

        /** Calculates a new Game Grind state für an Left-Swipe. Game Grid gets reversed for this. */
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

        /** Inline function to add a new Tile value. Returns a Tile to be added to GameGridTiles. */
        private fun Array<IntArray>.addTile(): Tile {
            val row = this.indices.random()
            val col = this.indices.random()
            if (this[row][col] != 0) {
                return this.addTile()
            } else {
                val newValue = if ((1..20).random() >= 18) 4 else 2
                this[row][col] = newValue
                return Tile(
                    cell = Cell(row, col),
                    value = newValue
                )
            }
        }

        /** Init a new Game with two starting tiles. */
        fun initNewGame(dimensions: Int): GameState {
            val initArray = Array(dimensions) { IntArray(dimensions) {0} }

            initArray.addTile()
            initArray.addTile()

            return GameState(dimensions = dimensions, score = 0, gameStateHolder = initArray)
        }
    }

    fun getTileValue(row: Int, column: Int): Int? {
        return if (column < dimensions && row < dimensions)
            gameStateHolder[row][column]
        else null
    }

    fun addRandomTile(): Tile {
        return gameStateHolder.addTile()
    }

    /**
     * Calculate the result of a move in the Game. The result contains the new GameState after the
     * move, the movements Tiles have to make during the move, and whether the move was valid.
     */
    fun makeMove(direction: SwipeDirection): GameStateUpdate {
        // perform the actual move
        val gridCalcResult =  when (direction) {
            SwipeDirection.UP -> calcSwipeUp(gameStateHolder)
            SwipeDirection.DOWN -> calcSwipeDown(gameStateHolder)
            SwipeDirection.LEFT -> calcSwipeLeft(gameStateHolder)
            SwipeDirection.RIGHT -> calcSwipeRight(gameStateHolder)
            SwipeDirection.NOOP -> GridCalcResult(gameStateHolder, TileMovements.noopMovements(gameStateHolder.size), 0)
        }

        val scoreChange = gridCalcResult.score
        val tileMovements = gridCalcResult.tileMovements
        val newGameStateHolder = gridCalcResult.gameStateHolder

        if (!newGameStateHolder.contentDeepEquals(gameStateHolder)) {
            return GameStateUpdate(
                gameState = this.copy(gameStateHolder = newGameStateHolder, score = this.score + scoreChange),
                tileMovements = tileMovements,
                validMove = true
            )
        }
        return GameStateUpdate(this, tileMovements, false)
    }

    /**
     * Check for the conditions to lose a Game. Calculates all possible next moves. The Game is lost
     * if all moves are invalid.
     */
    fun checkLostCondition(): Boolean {
        // check if the game was lost
        val nextUp = calcSwipeUp(gameStateHolder).gameStateHolder.contentDeepEquals(gameStateHolder)
        val nextDown = calcSwipeDown(gameStateHolder).gameStateHolder.contentDeepEquals(gameStateHolder)
        val nextLeft = calcSwipeLeft(gameStateHolder).gameStateHolder.contentDeepEquals(gameStateHolder)
        val nextRight = calcSwipeRight(gameStateHolder).gameStateHolder.contentDeepEquals(gameStateHolder)

        return (nextUp && nextRight && nextLeft && nextDown)
    }

    /** Check for a Cell with the value of 2048 to check for a win. */
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

    inline fun forEachCell(action: (cell: Cell, value: Int) -> Unit) {
        gameStateHolder.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, value ->
                action(Cell(rowIndex, columnIndex), value)
            }
        }
    }
}