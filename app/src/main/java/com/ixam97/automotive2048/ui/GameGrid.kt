package com.ixam97.automotive2048.ui

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ixam97.automotive2048.domain.GameGridState
import com.ixam97.automotive2048.domain.GameState
import com.ixam97.automotive2048.domain.SwipeDirection
import com.ixam97.automotive2048.domain.Tile
import com.ixam97.automotive2048.domain.TileMovements
import com.ixam97.automotive2048.ui.theme.getCellColor
import com.ixam97.automotive2048.utils.length
import kotlinx.coroutines.delay
import kotlin.math.absoluteValue

@Composable
fun GameGrid(
    gridDimensions: Int,
    onSwipe: (SwipeDirection) -> Unit,
    gameState: GameState,
    tileMovements: TileMovements,
    gameGridState: GameGridState
) {
    var startPosition by remember { mutableStateOf(Offset(0f, 0f)) }
    var currentPosition by remember { mutableStateOf(Offset(0f, 0f)) }
    var flinged by remember { mutableStateOf(false) }

    // var boxSize by remember { mutableStateOf(0.dp) }

    // val density = LocalDensity.current

    Box {
        Box (
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .sizeIn(maxHeight = 800.dp, maxWidth = 800.dp)
                .padding(20.dp)
                .aspectRatio(1f)
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            startPosition = offset
                        },
                        onDragEnd = {
                            if (!flinged) {
                                val xDelta = (startPosition.x - currentPosition.x)
                                val yDelta = (startPosition.y - currentPosition.y)
                                onSwipe(getDirectionFromDeltas(xDelta, yDelta))
                            }
                            flinged = false
                            startPosition = Offset(0f, 0f)
                        },
                        onDragCancel = {
                            flinged = false
                            startPosition = Offset(0f, 0f)
                        }
                    ) { change, _ ->
                        currentPosition = change.position
                        val xDelta = (startPosition.x - currentPosition.x)
                        val yDelta = (startPosition.y - currentPosition.y)
                        if ((xDelta.absoluteValue > 80 || yDelta.absoluteValue > 80) && !flinged) {
                            flinged = true
                            onSwipe(getDirectionFromDeltas(xDelta, yDelta))
                        }
                    }
                },
            // verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            GridBackground(gridDimensions)
            // GridTiles(gameState, gridDimensions, tileMovements, gameGridState)
            GridTilesNew(gridDimensions, gameGridState)
        }
    }
}

@Composable
private fun GridBackground(dimensions: Int) {
    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        for (row in 0 until dimensions) {
            Row(
                modifier = Modifier
                    .wrapContentSize(),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                for (column in 0 until dimensions) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(Color.White.copy(alpha = 0.05f))
                            .aspectRatio(1f)
                    )
                }
            }
        }
    }
}


@Composable
private fun GridTilesNew(
    dimensions: Int,
    gameGridState: GameGridState
) {
    BoxWithConstraints (
        modifier = Modifier
            .fillMaxSize()
    ){
        // val gridSize = (maxWidth.value.toInt() - maxWidth.value.toInt().mod(dimensions)).dp
        val boxSize = (maxWidth - 20.dp * (dimensions -1)) / dimensions

        gameGridState.tiles.forEachIndexed { index, tile ->
            GridTile(
                tile = tile,
                gameGridState = gameGridState,
                boxSize = boxSize
            )
        }

    }
}

private fun getDirectionFromDeltas(xDelta: Float, yDelta: Float): SwipeDirection {
    return if (xDelta.absoluteValue > yDelta.absoluteValue) {
        // Horizontal
        if (xDelta < 0) {
            SwipeDirection.RIGHT // "right"
        } else {
            SwipeDirection.LEFT // "left"
        }
    } else {
        // Vertical
        if (yDelta < 0) {
            SwipeDirection.DOWN // "down"
        } else {
            SwipeDirection.UP // "up"
        }
    }
}

@Composable
fun GridTile(
    tile: Tile,
    gameGridState: GameGridState,
    boxSize: Dp
) {
    val xOffset = ((boxSize + 20.dp) * tile.cell.col)
    val yOffset = ((boxSize + 20.dp) * tile.cell.row)
    val xOffsetAnim = animateDpAsState(((boxSize + 20.dp) * tile.cell.col))
    val yOffsetAnim = animateDpAsState(((boxSize + 20.dp) * tile.cell.row))

    Box (
        modifier = Modifier
            .size(boxSize)
            .offset(
                x = if (gameGridState.animate) xOffsetAnim.value else xOffset,
                y = if (gameGridState.animate) yOffsetAnim.value else yOffset
            ),
        contentAlignment = Alignment.Center
    ) {

        var visible by remember { mutableStateOf(tile.visible) }
        val size by animateDpAsState(
            if (visible) boxSize else 1.dp,
            animationSpec = tween(100)
        )
        // AnimatedVisibility(
        //     visible = true,
        //     enter = expandIn(animationSpec = tween(1000))
        // ) {
            Box(
                modifier = Modifier
                    .size(size)
                    .background(getCellColor(tile.value)),
                contentAlignment = Alignment.Center
            ) {
                val fontSize = ((boxSize.value / 2.5) * 3/tile.value.length().coerceAtLeast(3)).sp
                Text(
                    text = tile.value.toString(),
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    fontSize = fontSize
                )
            }
        // }
        LaunchedEffect(Unit) {
            delay(20)
            visible = true
        }
    }
}