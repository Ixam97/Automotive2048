package com.ixam97.automotive2048.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class DialogButton(
    val buttonText: String,
    val onClick: () -> Unit,
    val enabled: Boolean = true,
    val active: Boolean = false
)

@Composable
fun GameDialog(
    titleText: String,
    dialogButtons: List<DialogButton>,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f))
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Column (
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainer)
                .widthIn(max = 800.dp)
        ) {
            Column (
                modifier = Modifier
                    .padding(40.dp)
            ) {
                Text(
                    modifier = Modifier
                        .width(IntrinsicSize.Max),
                    text = titleText,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Spacer(Modifier.size(50.dp))
                content()
            }
            Row {
                dialogButtons.forEachIndexed { index, button ->
                    if (index > 0) Spacer(Modifier.size(5.dp))
                    Box (
                        modifier = Modifier
                            .weight(1f)
                            .background(
                                if (button.active) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                            )
                            .clickable(onClick =  button.onClick)
                            .padding(40.dp),
                        contentAlignment = Alignment.CenterStart
                    ) { Text(button.buttonText) }
                }
            }
        }
    }
}