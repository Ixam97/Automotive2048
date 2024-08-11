package com.ixam97.automotive2048

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.ixam97.automotive2048.domain.Cell
import com.ixam97.automotive2048.domain.GameState
import com.ixam97.automotive2048.domain.SwipeDirection
import com.ixam97.automotive2048.domain.Tile
import com.ixam97.automotive2048.ui.theme.getCellColor
import com.ixam97.automotive2048.ui.theme.polestarOrange
import com.ixam97.automotive2048.utils.length
import kotlin.math.absoluteValue

@Composable
fun GameGrid(
    modifier: Modifier = Modifier,
    gridDimensions: Pair<Int, Int>,
    onSwipe: (SwipeDirection) -> Unit,
    gameState: GameState
) {

    var log by remember { mutableStateOf("None") }

    var startPosition by remember { mutableStateOf(Offset(0f, 0f)) }
    var currentPosition by remember { mutableStateOf(Offset(0f, 0f)) }
    var flinged by remember { mutableStateOf(false) }

    var boxSize by remember { mutableStateOf(0.dp) }

    val density = LocalDensity.current

    Box {
        Box (
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(20.dp)
                .aspectRatio(1f)
                .fillMaxSize()
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
            /*
            for (row in 0 until gridDimensions.second) {
                Row (
                    modifier = Modifier
                        .wrapContentSize(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    for (column in 0 until gridDimensions.first) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .background(Color.White.copy(alpha = 0.05f))
                                .zIndex(2f)
                                .aspectRatio(1f),
                                // .onGloballyPositioned {
                                //     with(density) {
                                //         boxSize = (it.size.width.toDp())
                                //     }
                                // },
                            contentAlignment = Alignment.Center
                        ){
                            if (gameState.getTileValue(column, row) != 0) {
                                val value = gameState.getTileValue(column, row)
                                Box (
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .offset(x = 100.dp)
                                        .zIndex(1f)
                                        .background(getCellColor(value)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = value.toString())
                                }

                            }
                        }
                    }
                }
            }
            */
            GridBackground(4)
            GridTiles(gameState, 4)
        }
    }
}

@Composable
private fun GameTile(tile: Tile, size: Dp) {
    val backgroundColor = when (tile.value) {
        0 -> Color.White.copy(alpha = 0.05f)
        else -> polestarOrange
    }
    Box (
        modifier = Modifier
            .padding(
                start = (20.dp) + (20.dp + size) * tile.cell.col,
                top = (20.dp) + (20.dp + size) * tile.cell.row
            )
            .background(backgroundColor)
            .size(size),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tile.value.toString(),
            fontSize = 60.sp,
            fontWeight = FontWeight.Medium
        )
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
private fun GridTiles(gameState: GameState, dimensions: Int) {
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
                            .background(Color.Transparent)
                            .aspectRatio(1f)
                    ) {
                        val value = gameState.getTileValue(column, row)
                        if (value != 0 && value != null) {
                            BoxWithConstraints (
                                modifier = Modifier
                                    .fillMaxSize()
                                    .offset()
                                    .background(getCellColor(value)),
                                contentAlignment = Alignment.Center
                            ) {
                                val fontSize = ((maxWidth.value / 2.5) * 3/value.length().coerceAtLeast(3)).sp
                                Log.d("BOX", "Font Size: $fontSize")
                                Text(
                                    text = value.toString(),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = fontSize
                                )
                            }
                        }
                    }
                }
            }
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