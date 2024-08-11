package com.ixam97.automotive2048

import android.app.Application
import com.ixam97.automotive2048.repository.GameRepository

class Automotive2048: Application() {
    val gameRepository = GameRepository(applicationContext)
}