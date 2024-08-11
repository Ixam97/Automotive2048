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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ixam97.automotive2048.GameGrid
import com.ixam97.automotive2048.viewmodel.MainViewModel

@Composable
fun GameScreen(viewModel: MainViewModel, aspectRatio: Float) {
    Scaffold  { innerPadding ->

        var showRestartDialog by remember { mutableStateOf(false) }

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
                        onRestartClick = { viewModel.restartGame() },
                        onUndoClick = { viewModel.undoMove() },
                        aspectRatio = aspectRatio,
                        historySize = viewModel.historySize()
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
                        onRestartClick = { showRestartDialog = true },
                        onUndoClick = { viewModel.undoMove() },
                        aspectRatio = aspectRatio,
                        historySize = viewModel.historySize()
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
                    titleText = "GAME WON!",
                    dialogButtons = listOf(
                        DialogButton("Continue", { viewModel.dismissWin() }, active = true),
                        DialogButton("Restart Game", { viewModel.restartGame() })
                    )
                ) {
                    Text("You have created a tile with the value 2048 and won the game! You can now continue to play this session or start a new game.")
                }
            }

            if (showRestartDialog) {
                GameDialog(
                    titleText = "Restart Game?",
                    dialogButtons = listOf(
                        DialogButton("Restart", { viewModel.restartGame(); showRestartDialog = false }, active = true),
                        DialogButton( "Cancel", { showRestartDialog = false })
                    )
                ) { }
            }
            if (viewModel.gameLost) {
                GameDialog(
                    titleText = "GAME LOST!",
                    dialogButtons = listOf(
                        DialogButton("Restart", { viewModel.restartGame() }),
                        DialogButton("Undo Move", { viewModel.undoMove() })
                    )
                ) {
                    Text("You can either undo your last moves, or reset the game and start from the beginning!")
                }
            }

        }
    }
}