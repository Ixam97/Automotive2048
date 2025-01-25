package com.ixam97.automotive2048.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import android.os.Build
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ixam97.automotive2048.R
import com.ixam97.automotive2048.ui.components.MenuRow
import com.ixam97.automotive2048.ui.theme.iconButtonSize
import com.ixam97.automotive2048.viewmodel.MainViewModel
import com.ixam97.automotive2048.BuildConfig
import com.ixam97.automotive2048.repository.GameRepository
import com.ixam97.automotive2048.ui.components.SwitchRow
import com.ixam97.automotive2048.ui.theme.LinkViewer

@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    Box (
        modifier = Modifier
            .fillMaxSize()
    ) {
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
                        Spacer(Modifier.weight(1f))
                        IconButton(
                            modifier = Modifier.size(iconButtonSize),
                            onClick = {viewModel.openHowToPlayDialog()}
                        ) {
                            Icon(
                                modifier = Modifier.size(iconButtonSize),
                                imageVector = Icons.AutoMirrored.Filled.HelpOutline,
                                contentDescription = null
                            )
                        }

                    }
                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.primary,
                        thickness = 2.dp
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                val context = LocalContext.current
                Text(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(top = 24.dp, bottom = 10.dp),
                    color = MaterialTheme.colorScheme.primary,
                    text = stringResource(R.string.settings_title)
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 25.dp))
                SwitchRow(
                    title = stringResource(R.string.undo_enable_title),
                    onClick = { viewModel.toggleUndoButtonSetting() },
                    switchState = viewModel.undoButtonEnabled
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 25.dp))
                if (GameRepository.supportedOEMsList.contains(Build.BRAND)) {
                    SwitchRow(
                        title = stringResource(R.string.use_oem_theme_title),
                        onClick = { viewModel.toggleOemSchemeSetting() },
                        switchState = viewModel.oemSchemeEnabled
                    )
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 25.dp))
                }
                Text(
                    modifier = Modifier
                        .padding(horizontal = 24.dp)
                        .padding(top = 24.dp, bottom = 10.dp),
                    color = MaterialTheme.colorScheme.primary,
                    text = stringResource(R.string.about_title)
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 25.dp))

                val versionNameString = if (viewModel.sysInfoString.isBlank()) {
                    BuildConfig.VERSION_NAME
                } else {
                    "${BuildConfig.VERSION_NAME}\n${viewModel.sysInfoString}"
                }

                MenuRow(
                    title = stringResource(R.string.version_headline),
                    text = versionNameString,
                    onClick = {
                        viewModel.versionClick(context)
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 25.dp))
                MenuRow(
                    title = stringResource(R.string.copyright_headline),
                    text = "©2024 Maximilian Goldschmidt"
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 25.dp))
                MenuRow(
                    title = stringResource(R.string.github_headline),
                    text = stringResource(R.string.github_text),
                    external = true,
                    browsable = true,
                    onClick = {
                        if (BuildConfig.FLAVOR == "link") {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(context.getString(R.string.github_url))
                                )
                            )
                        } else {
                            viewModel.openLinkViewer(1)
                        }
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 25.dp))
                MenuRow(
                    title = stringResource(R.string.inspired_headline),
                    text = stringResource(R.string.inspired_text),
                    external = true,
                    browsable = true,
                    onClick = {
                        if (BuildConfig.FLAVOR == "link") {
                            context.startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse(context.getString(R.string.inspired_url))
                                )
                            )
                        } else {
                            viewModel.openLinkViewer(2)
                        }
                    }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 25.dp))
                MenuRow(
                    title = stringResource(R.string.licenses_headline),
                    browsable = true,
                    onClick = { viewModel.licensesClicked() }
                )
            }
        }
        if (viewModel.showHowToPlayDialog) {
            GameDialog(
                titleText = stringResource(R.string.dialog_how_to_title),
                dialogButtons = listOf(
                    DialogButton(stringResource(R.string.dialog_how_to_button_close), { viewModel.closeHowToPlayDialog() }, active = true),

                    )
            ) {
                Text(stringResource(R.string.dialog_how_to_text))
            }
        }

        if (viewModel.link != 0) {
            when (viewModel.link) {
                1  -> {
                    LinkViewer(
                        url = R.string.github_url,
                        qr = R.drawable.qr_github
                    ) { viewModel.closeLinkViewer() }
                }
                2  -> {
                    LinkViewer(
                        url = R.string.inspired_url,
                        qr = R.drawable.qr_inspired
                    ) { viewModel.closeLinkViewer() }
                }
                else -> viewModel.closeLinkViewer()
            }

        }
    }
}