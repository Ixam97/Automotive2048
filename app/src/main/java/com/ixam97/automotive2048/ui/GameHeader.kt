package com.ixam97.automotive2048.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtMost
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.ixam97.automotive2048.R
import com.ixam97.automotive2048.ui.theme.disabledTextColor
import com.ixam97.automotive2048.ui.theme.iconButtonSize

private val scoreCardHeight = 130.dp
private val dividerThickness = 5.dp
private val dividerPadding = 15.dp
private val spacerSize = 20.dp
private val iconHeight = iconButtonSize

private val verticalHeaderHeight = scoreCardHeight * 2 + iconHeight + spacerSize * 2 + dividerThickness + dividerPadding * 2

@Composable
fun GameHeader(
    score: Int,
    highscore: Int,
    onSettingsClick: () -> Unit,
    onRestartClick: () -> Unit,
    onUndoClick: () -> Unit,
    historySize: Int,
    aspectRatio: Float,
    allowUndo: Boolean
) {
    BoxWithConstraints {
        if (aspectRatio > 1) {
            val height = maxHeight
            Column(
                modifier = Modifier
                    .widthIn(max = maxWidth.coerceAtMost(400.dp))
                    .onGloballyPositioned { }
            ) {
                TitleCard(availableHeight = height - verticalHeaderHeight)
                HorizontalDivider(
                    modifier = Modifier
                        .padding(vertical = dividerPadding)
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    thickness = dividerThickness
                )
                ButtonRow(
                    modifier = Modifier.fillMaxWidth(),
                    onSettingsClick = onSettingsClick,
                    onResetClick = onRestartClick,
                    onUndoClick = onUndoClick,
                    historySize = historySize,
                    allowUndo = allowUndo
                )
                Spacer(Modifier.size(spacerSize))
                ScoreCard(
                    modifier = Modifier.fillMaxWidth(),
                    name = stringResource(R.string.score),
                    value = score,
                    height = scoreCardHeight
                )
                Spacer(Modifier.size(spacerSize))
                ScoreCard(
                    modifier = Modifier.fillMaxWidth(),
                    name = stringResource(R.string.highscore),
                    value = highscore,
                    height = scoreCardHeight
                )
            }
        } else {
            val width = maxWidth

            val scoreAreaWidth = maxWidth * 0.7f

            val adjustedScoreCardHeight = if (scoreAreaWidth > 600.dp) {
                scoreCardHeight
            } else {
                scoreCardHeight * (scoreAreaWidth.value / 600f)
            }

            println("Adjusted Score Card Height: $adjustedScoreCardHeight (${adjustedScoreCardHeight.value}")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max)
            ) {
                TitleCard(
                    modifier = Modifier
                       .width(width * 0.3f),
                    availableHeight = min(scoreCardHeight + spacerSize + iconHeight, (width * 0.3f) / 1.35f)
                )
                VerticalDivider(
                    modifier = Modifier.padding(horizontal = dividerPadding),
                    color = MaterialTheme.colorScheme.primary,
                    thickness = dividerThickness
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    ButtonRow(
                        modifier = Modifier.fillMaxWidth(),
                        onSettingsClick = onSettingsClick,
                        onResetClick = onRestartClick,
                        onUndoClick = onUndoClick,
                        historySize = historySize,
                        allowUndo = allowUndo
                    )
                    Spacer(Modifier.size(20.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ScoreCard(
                            modifier = Modifier
                                .weight(1f), stringResource(R.string.score),
                            value = score,
                            height = adjustedScoreCardHeight
                        )
                        Spacer(Modifier.size(20.dp))
                        ScoreCard(
                            modifier = Modifier
                                .weight(1f), stringResource(R.string.highscore),
                            value = highscore,
                            height = adjustedScoreCardHeight
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TitleCard(
    modifier: Modifier = Modifier,
    availableHeight: Dp
) {

    // var titleSize by remember { mutableStateOf(120.sp) }
    // var textOverflow by remember { mutableStateOf(false) }
    // val localDensity = LocalDensity.current

    Box(
        modifier = modifier
            .height(availableHeight.coerceAtMost(174.dp))
            // .widthIn(min = 300.dp)
    ) {
        println(" Screen density: ${LocalDensity.current.density}")
        Column {
            Text(
                text = "2048",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = (availableHeight.value * 0.6).coerceAtMost(120.0).sp
                )
            )
            Text(
                text = stringResource(R.string.gametype_endless),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontSize = (availableHeight.value * 0.15).coerceAtMost(30.0).sp
                ),
                color = disabledTextColor
            )
        }
    }

}

@Composable
private fun ScoreCard(
    modifier: Modifier = Modifier,
    name: String,
    value: Int,
    height: Dp) {

    val fontScalingFactor = height.value / scoreCardHeight.value
    println("Font scaling factor = $fontScalingFactor")

    Column (
        modifier = modifier
            .height(height)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 30.dp * fontScalingFactor),
        verticalArrangement = Arrangement.Center
    ) {
        val defaultValueTextStyle = MaterialTheme.typography.titleMedium.copy(fontSize = 55.sp * fontScalingFactor)
        var valueTextStyle by remember { mutableStateOf(defaultValueTextStyle) }

        val defaultNameTextStyle = MaterialTheme.typography.titleMedium.copy(fontSize = 27.sp * fontScalingFactor)
        var nameTextStyle by remember { mutableStateOf(defaultNameTextStyle) }

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text =  value.toString(),
            style = valueTextStyle,
            softWrap = false,
            onTextLayout = { textLayoutResult ->
                if (textLayoutResult.didOverflowWidth) {
                    valueTextStyle = valueTextStyle.copy(fontSize = valueTextStyle.fontSize * 0.9)
                }
            }
        )
        Text(
            text = name,
            style = MaterialTheme.typography.titleSmall.copy(fontSize = 27.sp * fontScalingFactor),
            color = MaterialTheme.colorScheme.primary,
            softWrap = false,
            onTextLayout = { textLayoutResult ->
                if (textLayoutResult.didOverflowWidth) {
                    nameTextStyle = nameTextStyle.copy(fontSize = nameTextStyle.fontSize * 0.9)
                }
            }
        )
    }
}

@Composable
private fun ButtonRow(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit,
    onResetClick: () -> Unit,
    onUndoClick: () -> Unit,
    historySize: Int,
    allowUndo: Boolean
) {
    Row (
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (allowUndo) {
            Text(historySize.toString(), color = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.size(10.dp))
            IconButton(
                modifier = Modifier.size(iconHeight),
                onClick = onUndoClick,
                enabled = historySize > 0
            ) {
                Icon(
                    modifier = Modifier.size(iconHeight),
                    imageVector = Icons.AutoMirrored.Filled.Undo,
                    contentDescription = null
                )
            }
            Spacer(Modifier.size(20.dp))
        }
        IconButton(
            modifier = Modifier.size(iconHeight),
            onClick = onResetClick
        ) {
            Icon(
                modifier = Modifier.size(iconHeight),
                imageVector = Icons.Default.Sync,
                contentDescription = null
            )
        }
        Spacer(Modifier.size(20.dp))
        IconButton(
            modifier = Modifier.size(iconHeight),
            onClick = onSettingsClick
        ) {
            Icon(
                modifier = Modifier.size(iconHeight),
                imageVector = Icons.Default.Menu,
                contentDescription = null
            )
        }
    }
}