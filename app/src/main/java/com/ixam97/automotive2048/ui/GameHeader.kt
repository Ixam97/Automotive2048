package com.ixam97.automotive2048.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ixam97.automotive2048.R
import com.ixam97.automotive2048.ui.theme.disabledTextColor
import com.ixam97.automotive2048.ui.theme.iconButtonSize

@Composable
fun GameHeader(
    score: Int,
    highscore: Int,
    onSettingsClick: () -> Unit,
    onResetClick: () -> Unit,
    onUndoClick: () -> Unit,
    historySize: Int,
    aspectRatio: Float
) {
    if (aspectRatio > 1) {
        Column (
            modifier = Modifier.width(IntrinsicSize.Min)
        ) {
            TitleCard()
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 15.dp).fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                thickness = 5.dp
            )
            ButtonRow (
                modifier = Modifier.fillMaxWidth(),
                onSettingsClick = onSettingsClick,
                onResetClick = onResetClick,
                onUndoClick = onUndoClick,
                historySize = historySize
            )
            Spacer(Modifier.size(20.dp))
            ScoreCard(
                modifier = Modifier.fillMaxWidth(),
                name = stringResource(R.string.score),
                value = score
            )
            Spacer(Modifier.size(20.dp))
            ScoreCard(
                modifier = Modifier.fillMaxWidth(),
                name = stringResource(R.string.highscore),
                value = highscore
            )
        }
    } else {
        Row (
            modifier = Modifier
                . fillMaxWidth()
                .height(IntrinsicSize.Max)
        ) {
            TitleCard()
            VerticalDivider(
                modifier = Modifier.padding(horizontal = 15.dp),
                color = MaterialTheme.colorScheme.primary,
                thickness = 5.dp
            )
            Column (
                modifier = Modifier.weight(1f)
            ) {
                ButtonRow(
                    modifier = Modifier.fillMaxWidth(),
                    onSettingsClick = onSettingsClick,
                    onResetClick = onResetClick,
                    onUndoClick = onUndoClick,
                    historySize = historySize
                )
                Spacer(Modifier.size(20.dp))
                Row (
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ScoreCard( modifier = Modifier.fillMaxHeight().weight(1f), stringResource(R.string.score), score)
                    Spacer(Modifier.size(20.dp))
                    ScoreCard( modifier = Modifier.fillMaxHeight().weight(1f), stringResource(R.string.highscore), highscore)
                }
            }
        }
    }
}

@Composable
private fun TitleCard() {
    Column {
        Text(
            text = "2048",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(R.string.gametype_endless),
            style = MaterialTheme.typography.titleSmall,
            color = disabledTextColor
        )
    }
}

@Composable
private fun ScoreCard(modifier: Modifier = Modifier, name: String, value: Int) {
    Column (
        modifier = modifier
            .height(IntrinsicSize.Max)
            .background(MaterialTheme.colorScheme.surface)
            .padding(vertical = 15.dp, horizontal = 30.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        val defaultTextStyle = MaterialTheme.typography.titleMedium
        var textStyle by remember { mutableStateOf(defaultTextStyle) }
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text =  value.toString(),
            style = textStyle,
            softWrap = false,
            onTextLayout = { textLayoutResult ->
                if (textLayoutResult.didOverflowWidth) {
                    textStyle = textStyle.copy(fontSize = textStyle.fontSize * 0.9)
                }
            }
        )
        Text(name, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
private fun ButtonRow(
    modifier: Modifier = Modifier,
    onSettingsClick: () -> Unit,
    onResetClick: () -> Unit,
    onUndoClick: () -> Unit,
    historySize: Int
) {
    Row (
        modifier = modifier,
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(historySize.toString(), color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.size(10.dp))
        IconButton(
            modifier = Modifier.size(iconButtonSize),
            onClick = onUndoClick,
            enabled = historySize > 0
        ) {
            Icon(
                modifier = Modifier.size(iconButtonSize),
                imageVector = Icons.AutoMirrored.Filled.Undo,
                contentDescription = null
            )
        }
        Spacer(Modifier.size(20.dp))
        IconButton(
            modifier = Modifier.size(iconButtonSize),
            onClick = onResetClick
        ) {
            Icon(
                modifier = Modifier.size(iconButtonSize),
                imageVector = Icons.Default.Sync,
                contentDescription = null
            )
        }
        Spacer(Modifier.size(20.dp))
        IconButton(
            modifier = Modifier.size(iconButtonSize),
            onClick = onSettingsClick
        ) {
            Icon(
                modifier = Modifier.size(iconButtonSize),
                imageVector = Icons.Default.Menu,
                contentDescription = null
            )
        }
    }
}