package com.ixam97.automotive2048.ui.theme

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.ixam97.automotive2048.R
import com.ixam97.automotive2048.ui.DialogButton
import com.ixam97.automotive2048.ui.GameDialog

@Composable
fun LinkViewer(@StringRes url: Int, @DrawableRes qr: Int, closeAction: () -> Unit) {
    GameDialog(
        titleText =  stringResource(url),
        dialogButtons = listOf(
            DialogButton(
                buttonText = stringResource(R.string.dialog_how_to_button_close),
                onClick = closeAction
            )
        ),
        scrollable = false
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                // modifier = Modifier.fillMaxSize(),
                painter =  painterResource(qr),
                contentDescription = null,
                contentScale = ContentScale.Fit
            )
        }
    }
}