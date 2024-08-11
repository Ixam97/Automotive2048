package com.ixam97.automotive2048

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ixam97.automotive2048.domain.GameState
import com.ixam97.automotive2048.repository.GameRepository
import com.ixam97.automotive2048.ui.GameScreen
import com.ixam97.automotive2048.ui.SettingsScreen
import com.ixam97.automotive2048.ui.theme.Automotive2048Theme
import com.ixam97.automotive2048.viewmodel.MainViewModel
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Automotive2048)

        setContent {

            val viewModel: MainViewModel = viewModel {
                MainViewModel(GameRepository(this@MainActivity))
            }

            var aspectRatio by remember { mutableFloatStateOf(1f) }

            Automotive2048Theme ( uniqueTheme = if (viewModel.oemSchemeEnabled) Build.BRAND else "" ) {
                Box(modifier = Modifier.fillMaxSize().onSizeChanged { size ->
                    aspectRatio = size.width.toFloat() / size.height.toFloat()
                }) {
                    when (viewModel.currentScreenIndex) {
                        1 -> SettingsScreen(viewModel)
                        else -> GameScreen(viewModel, aspectRatio)
                    }
                }
            }
        }
    }
}

private fun generateTestRows(num: Int, size: Int, maxPower: Int = 5) : List<List<Int>> {
    val rowsList = mutableListOf<List<Int>>()

    for (i in 0 until num) {
        val row = mutableListOf<Int>()
        for (j in 0 until size) {
            val power = (0..maxPower).random()
            var value = 2f.pow(power).toInt()
            if (value == 1) value = 0
            row.add(value)
        }
        rowsList.add(row)
    }
    return rowsList
}