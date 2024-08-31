package com.ixam97.automotive2048.ui.theme

import android.util.Log
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import java.io.File

val polestarFont = FontFamily(
    Font(
        File("/product/fonts/PolestarUnica77-Regular.otf"),
        weight = FontWeight.Normal
    ),
    Font(
        File("/product/fonts/PolestarUnica77-Medium.otf"),
        weight = FontWeight.Medium
    )
)

val Typography = Typography(
    headlineLarge = TextStyle(
        fontSize = 48.sp
    ),
    titleLarge = TextStyle(
        fontSize = 38.sp
    ),
    titleMedium = TextStyle(
        fontSize = 31.sp
    ),
    titleSmall = TextStyle(
        fontSize = 30.sp
    ),
    bodyLarge = TextStyle(fontSize = 31.sp),
    bodyMedium = TextStyle(fontSize = 31.sp),
    bodySmall = TextStyle(fontSize = 25.sp),
    labelSmall = TextStyle(fontSize = 25.sp),
    labelLarge = TextStyle(fontSize = 31.sp)
)

val defaultPolestarTypography = try {
    if (File("/product/fonts/PolestarUnica77-Regular.otf").exists() && File("/product/fonts/PolestarUnica77-Medium.otf").exists()) {
        Typography(
            displayLarge = Typography.displayLarge.copy(fontFamily = polestarFont),
            displayMedium = Typography.displayMedium.copy(fontFamily = polestarFont),
            displaySmall = Typography.displaySmall.copy(fontFamily = polestarFont),

            headlineLarge = Typography.headlineLarge.copy(fontFamily = polestarFont),
            headlineMedium = Typography.headlineMedium.copy(fontFamily = polestarFont),
            headlineSmall = Typography.headlineSmall.copy(fontFamily = polestarFont),

            titleLarge = Typography.titleLarge.copy(fontFamily = polestarFont,),
            titleMedium = Typography.titleMedium.copy(fontFamily = polestarFont),
            titleSmall = Typography.titleSmall.copy(fontFamily = polestarFont),

            bodyLarge = Typography.bodyLarge.copy(fontFamily = polestarFont),
            bodyMedium = Typography.bodyMedium.copy(fontFamily = polestarFont),
            bodySmall = Typography.bodySmall.copy(fontFamily = polestarFont),

            labelLarge = Typography.labelLarge.copy(fontFamily = polestarFont),
            labelMedium = Typography.labelMedium.copy(fontFamily = polestarFont),
            labelSmall = Typography.labelSmall.copy(fontFamily = polestarFont)
        )
    } else {
        Log.w("TYPE", "Polestar Font not found")
        Typography
    }
} catch (e: Exception) {
    Log.e("TYPE", "Polestar Font not found")
    Typography
}

