package com.ixam97.automotive2048.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
                        .wrapContentWidth()
                        .padding(25.dp)
                ) {
                    GameGrid(
                        gridDimensions = viewModel.gameState.dimensions,
                        onSwipe = {dir -> viewModel.swiped(dir)},
                        gameGridTiles = viewModel.gameGridTiles
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
                        gridDimensions = viewModel.gameState.dimensions,
                        onSwipe = {dir -> viewModel.swiped(dir)},
                        gameGridTiles = viewModel.gameGridTiles
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

            if (viewModel.showHowToPlayDialog) {
                GameDialog(
                    titleText = stringResource(R.string.dialog_how_to_title),
                    dialogButtons = listOf(
                        DialogButton(stringResource(R.string.dialog_how_to_button_begin), { viewModel.closeHowToPlayDialog() }, active = true),

                    )
                ) {
                    Text(stringResource(R.string.dialog_how_to_text))
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
                        DialogButton(stringResource(R.string.dialog_lost_restart), { viewModel.restartGame() }, active = true),
                        DialogButton(stringResource(R.string.dialog_lost_undo), { viewModel.undoMove() })
                    )
                ) {
                    Text(stringResource(R.string.dialog_lost_text))
                }
            }

        }
    }
}