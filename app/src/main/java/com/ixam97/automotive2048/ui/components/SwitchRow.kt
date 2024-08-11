package com.ixam97.automotive2048.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

@Composable
fun SwitchRow(
    title: String,
    text: String? = null,
    onClick: () -> Unit,
    switchState: Boolean,
    enabled: Boolean = true
) {
    MenuRow(
        title = title,
        text = text,
        onClick = onClick,
        enabled = enabled,
        trailingContent = {
            Switch(
                modifier = Modifier
                    .graphicsLayer(
                        transformOrigin = TransformOrigin(1f,.5f),
                        scaleX = 1.5f,
                        scaleY = 1.5f
                    ),
                colors = SwitchDefaults.colors(
                    uncheckedTrackColor = Color.Transparent,
                    disabledUncheckedTrackColor = Color.Transparent,
                    disabledCheckedThumbColor = MaterialTheme.colorScheme.surface
                ),
                checked = switchState,
                onCheckedChange = null,
                enabled = enabled
            )
        }
    )
}