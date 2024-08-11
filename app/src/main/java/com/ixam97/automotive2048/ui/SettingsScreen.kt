package com.ixam97.automotive2048.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ixam97.automotive2048.R
import com.ixam97.automotive2048.ui.components.MenuRow
import com.ixam97.automotive2048.ui.theme.iconButtonSize
import com.ixam97.automotive2048.viewmodel.MainViewModel
import com.ixam97.automotive2048.BuildConfig
import com.ixam97.automotive2048.ui.components.SwitchRow
import com.ixam97.automotive2048.ui.theme.cell1024Color
import com.ixam97.automotive2048.ui.theme.cell128Color
import com.ixam97.automotive2048.ui.theme.cell16Color
import com.ixam97.automotive2048.ui.theme.cell2Color

@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    Scaffold (
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(
                        modifier = Modifier.size(iconButtonSize),
                        onClick = {viewModel.settingsBackClicked()}
                    ) {
                        Icon(
                            modifier = Modifier.size(iconButtonSize),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null
                        )
                    }
                    Spacer(Modifier.size(40.dp))
                    Text(stringResource(R.string.app_name), style = MaterialTheme.typography.headlineLarge)
                }
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.primary,
                    thickness = 2.dp
                )
            }
        }
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            /*
            Text(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp, bottom = 10.dp),
                color = MaterialTheme.colorScheme.primary,
                text = "Settings"
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 25.dp))
            SwitchRow(
                title = "Test Switch",
                onClick = { switchState = !switchState},
                switchState = switchState
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 25.dp))
             */
            Text(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 24.dp, bottom = 10.dp),
                color = MaterialTheme.colorScheme.primary,
                text = "About 2048 Automotive"
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 25.dp))
            MenuRow(
                title = "Version",
                text = BuildConfig.VERSION_NAME
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 25.dp))
            MenuRow(
                title = "Copyright",
                text = "©2024 Maximilian Goldschmidt"
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 25.dp))
            MenuRow(
                title = "GitHub",
                external = true,
                browsable = true,
                onClick = {}
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 25.dp))
            MenuRow(
                title = "Licenses",
                browsable = true,
                onClick = {}
            )
        }
    }
}