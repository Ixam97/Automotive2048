package com.ixam97.automotive2048.ui.theme

import androidx.compose.ui.graphics.Color

val polestarOrange = Color(0xFFD96C00)
val polestarSurface = Color(0xff1f1f1f)// Color(0xff090909)
val polestarDarkSurface = Color(0xFF0F0F0F)
val volvoBlue = Color(0xFF2F6093)

val disabledTextColor = Color(0xFF757575)
val disabledOverlayColor = Color(0x8A000000)

val darkBackground = Color(0xFF1B1B1B)
val mainBackground = Color(0xFF1F1F1F)
val headerBackground = Color(0xFF282A2D)
val primaryButtonGray = Color(0xFF3E4146)
val primaryGray = Color(0xFF7B858A)
val secondaryGray = Color(0xFFA3B1B8)

val clubBlueLight = Color(0xff60bcd5)
val clubBlue = Color(0xff447ea6)
val clubBlueDeep = Color(0xff0c4b5d)
val clubBlueDark = Color(0xff161d39)
val clubVioletLight = Color(0xff8480bd)
val clubViolet = Color(0xff77347b)
val clubVioletDark = Color(0xff2a1037)
val clubLight = Color(0xffefefef)
val clubMedium = Color(0xff707a83)
val clubNightVariant = Color(0xff111922)
val clubNight = Color(0xff0a0f14)
val clubNightDarker = Color(0xff05080C)
val clubHint = Color(0xfff7ea48)

val badRed = Color(0xffEB1717)


val cell2Color = Color(0xFF484F52)
val cell4Color = Color(0xFF4E5C64)
val cell8Color = Color(0xFF546B79)
val cell16Color = Color(0xFF5A7A8D)
val cell32Color = Color(0xFF575B6A)
val cell64Color = Color(0xFF553E49)
val cell128Color = Color(0xFF53242C)
val cell256Color = Color(0xFF7F4734)
val cell512Color = Color(0xFFA8683C)
val cell1024Color = Color(0xFFD58C44)

// Cell colors (Club Gradient)
/*
val cell2Color = clubViolet
val cell4Color = Color(0xFF723B7E)
val cell8Color = Color(0xff6D4283)
val cell16Color = Color(0xFF664C8A)
val cell32Color = Color(0xFF5D5990)
val cell64Color = Color(0xFF566396)
val cell128Color = Color(0xFF566396)
val cell256Color = Color(0xFF566396)
val cell512Color = clubBlue
val cell1024Color = Color(0xFFD58C44)
*/
// Original Color Set
/*
val cell2Color = Color(0xFF2D241A)
val cell4Color = Color(0xFF191107)
val cell8Color = Color(0xFF110C05)
val cell16Color = Color(0xFF3A220E)
val cell32Color = Color(0xFF281504)
val cell64Color = Color(0xFF170B00)
val cell128Color = Color(0xFF172B30)
val cell256Color = Color(0xFF0E1B1E)
val cell512Color = Color(0xFF000F13)
val cell1024Color = Color(0xFFD58C44)
*/

fun getCellColor(cellValue: Int?) = when (cellValue) {
    0, null -> Color.Transparent
    2 -> cell2Color
    4 -> cell4Color
    8 -> cell8Color
    16 -> cell16Color
    32 -> cell32Color
    64 -> cell64Color
    128 -> cell128Color
    256 -> cell256Color
    512 -> cell512Color
    1024 -> cell1024Color
    else -> polestarOrange
}

