package com.ixam97.automotive2048.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ixam97.automotive2048.GameGrid
import com.ixam97.automotive2048.R
import com.ixam97.automotive2048.viewmodel.MainViewModel

@Composable
fun GameScreen(viewModel: MainViewModel, aspectRatio: Float) {
    Scaffold  { innerPadding ->

        Box (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            if (aspectRatio > 1f) {
                Row (
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .padding(25.dp)
                ) {
                    GameGrid(
                        modifier = Modifier
                            .weight(1f)
                            .sizeIn(
                                maxHeight = 800.dp,
                                maxWidth = 800.dp
                            ),
                        gridDimensions = Pair(4, 4),
                        onSwipe = {dir -> viewModel.swiped(dir)},
                        gameState = viewModel.gameState
                    )
                    Spacer(Modifier.size(20.dp))
                    GameHeader(
                        score = viewModel.gameState.score,
                        highscore = viewModel.highscore,
                        onSettingsClick = { viewModel.settingsClicked() },
                        onRestartClick = { viewModel.showRestartDialog() },
                        onUndoClick = { viewModel.undoMove() },
                        aspectRatio = aspectRatio,
                        historySize = viewModel.historySize(),
                        allowUndo = viewModel.undoButtonEnabled
                    )
                }
            } else {
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(25.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    GameHeader(
                        score = viewModel.gameState.score,
                        highscore = viewModel.highscore,
                        onSettingsClick = { viewModel.settingsClicked() },
                        onRestartClick = { viewModel.showRestartDialog() },
                        onUndoClick = { viewModel.undoMove() },
                        aspectRatio = aspectRatio,
                        historySize = viewModel.historySize(),
                        allowUndo = viewModel.undoButtonEnabled
                    )
                    Spacer(Modifier.size(20.dp))
                    GameGrid(
                        modifier = Modifier
                            .weight(1f)
                            .sizeIn(
                                maxHeight = 1000.dp,
                                maxWidth = 1000.dp
                            ),
                        gridDimensions = Pair(4, 4),
                        onSwipe = {dir -> viewModel.swiped(dir)},
                        gameState = viewModel.gameState
                    )
                }
            }

            if (viewModel.gameWon && !viewModel.gameWinDismissed) {
                GameDialog(
                    titleText = stringResource(R.string.dialog_won_title),
                    dialogButtons = listOf(
                        DialogButton(stringResource(R.string.dialog_won_continue), { viewModel.dismissWin() }, active = true),
                        DialogButton(stringResource(R.string.dialog_won_restart), { viewModel.restartGame() })
                    )
                ) {
                    Text(stringResource(R.string.dialog_won_text))
                }
            }

            if (viewModel.showRestartDialog) {
                GameDialog(
                    titleText = stringResource(R.string.dialog_reset_title),
                    dialogButtons = listOf(
                        DialogButton(stringResource(R.string.dialog_reset_confirm), { viewModel.restartGame(); viewModel.closeRestartDialog() }, active = true),
                        DialogButton(stringResource(R.string.dialog_reset_cancel), { viewModel.closeRestartDialog() })
                    )
                ) { }
            }
            if (viewModel.gameLost) {
                GameDialog(
                    titleText = stringResource(R.string.dialog_lost_title),
                    dialogButtons = listOf(
                        DialogButton(stringResource(R.string.dialog_lost_restart), { viewModel.restartGame() }),
                        DialogButton(stringResource(R.string.dialog_lost_undo), { viewModel.undoMove() })
                    )
                ) {
                    Text(stringResource(R.string.dialog_lost_text))
                }
            }

        }
    }
}