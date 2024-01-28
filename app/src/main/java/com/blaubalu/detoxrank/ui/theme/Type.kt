package com.blaubalu.detoxrank.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.blaubalu.detoxrank.R

/**
 * Typography powered by Google Fonts library
 */

val DMSans = FontFamily(
    Font(R.font.dm_sans_regular),
    Font(R.font.dm_sans_bold),
    Font(R.font.dm_sans_medium)
)

val JosefinSans = FontFamily(
    Font(R.font.josefin_sans_bold),
    Font(R.font.josefin_sans_regular),
    Font(R.font.josefin_sans_thin)
)

/**
 * specifications and edits of individual fonts
 */
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = DMSans,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 32.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = DMSans
    ),
    bodySmall = TextStyle(
        fontFamily = DMSans,
        fontStyle = FontStyle.Italic
    ),
    titleMedium = TextStyle(
        fontFamily = JosefinSans,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    titleLarge = TextStyle(
        fontFamily = DMSans,
        fontWeight = FontWeight.Bold,
        fontSize = 25.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = JosefinSans,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = JosefinSans,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = JosefinSans,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp
    )
)