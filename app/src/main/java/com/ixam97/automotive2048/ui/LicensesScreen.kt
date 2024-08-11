package com.ixam97.automotive2048.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.ixam97.automotive2048.ui.theme.iconButtonSize
import com.ixam97.automotive2048.viewmodel.MainViewModel
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.LibraryDefaults

@Composable
fun LicensesScreen(
    viewModel: MainViewModel
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
                        onClick = {viewModel.licensesBackClicked()}
                    ) {
                        Icon(
                            modifier = Modifier.size(iconButtonSize),
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null
                        )
                    }
                    Spacer(Modifier.size(40.dp))
                    Text(stringResource(R.string.licenses_headline), style = MaterialTheme.typography.headlineLarge)
                }
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.primary,
                    thickness = 2.dp
                )
            }
        }
    ) { innerPadding ->
        LibrariesContainer(
            modifier = Modifier.padding(innerPadding),
            padding = LibraryDefaults.libraryPadding(badgeContentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp)),
            // colors = LibraryDefaults.libraryColors(backgroundColor = MaterialTheme.colorScheme.surfaceContainer)
        )
    }
}