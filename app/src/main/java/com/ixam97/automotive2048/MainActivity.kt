package com.ixam97.automotive2048

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ixam97.automotive2048.repository.GameRepository
import com.ixam97.automotive2048.ui.GameScreen
import com.ixam97.automotive2048.ui.LicensesScreen
import com.ixam97.automotive2048.ui.SettingsScreen
import com.ixam97.automotive2048.ui.theme.Automotive2048Theme
import com.ixam97.automotive2048.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Automotive2048)

        setContent {

            val viewModel: MainViewModel = viewModel {
                MainViewModel(GameRepository(this@MainActivity))
            }

            Automotive2048Theme ( uniqueTheme = if (viewModel.oemSchemeEnabled) Build.BRAND else "" ) {
                BoxWithConstraints (modifier = Modifier.fillMaxSize()
                ) {
                    val aspectRatio = maxWidth / maxHeight
                    when (viewModel.currentScreenIndex) {
                        1 -> SettingsScreen(viewModel)
                        2 -> LicensesScreen(viewModel)
                        else -> GameScreen(viewModel, aspectRatio)
                    }
                }
            }
        }
    }
}