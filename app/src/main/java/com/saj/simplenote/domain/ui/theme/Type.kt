package com.saj.simplenote.domain.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.saj.simplenote.R


private val Inter = FontFamily(
    Font(R.font.inter_regular, FontWeight.Normal),
    Font(R.font.inter_medium, FontWeight.Medium),
    Font(R.font.inter_bold, FontWeight.Bold),
    Font(R.font.inter_black, FontWeight.Black)
)

//val SimpleNoteTypography = Typography(
//    h1 = TextStyle(
//        fontFamily = Inter,
//        fontWeight = FontWeight.Black,
//        fontSize = 30.sp,
//        platformStyle = PlatformTextStyle(
//            includeFontPadding = false
//        ),
//    ),
//    h2 = TextStyle(
//        fontFamily = Inter,
//        fontWeight = FontWeight.Bold,
//        fontSize = 43.sp,
//        platformStyle = PlatformTextStyle(
//            includeFontPadding = false
//        ),
//    ),
//    h3 = TextStyle(
//        fontFamily = Inter,
//        fontWeight = FontWeight.Medium,
//        fontSize = 24.sp,
//        platformStyle = PlatformTextStyle(
//            includeFontPadding = false
//        ),
//    ),
//    h4 = TextStyle(
//        fontFamily = Inter,
//        fontWeight = FontWeight.Normal,
//        fontSize = 20.sp,
//        platformStyle = PlatformTextStyle(
//            includeFontPadding = false
//        ),
//    ),
//    h5 = TextStyle(
//        fontFamily = Inter,
//        fontSize = 22.sp,
//        platformStyle = PlatformTextStyle(
//            includeFontPadding = false
//        ),
//        fontWeight = FontWeight.Bold
//    ),
//    h6 = TextStyle(
//        fontFamily = Inter,
//        fontSize = 21.sp,
//        fontWeight = FontWeight.Bold,
//    ),
//    button = TextStyle(
//        fontFamily = Inter,
//        fontWeight = FontWeight.Bold,
//        fontSize = 18.sp,
//        platformStyle = PlatformTextStyle(
//            includeFontPadding = false
//        )
//    ),
//    caption = TextStyle(
//        fontFamily = Inter,
//        fontWeight = FontWeight.Thin,
//        platformStyle = PlatformTextStyle(
//            includeFontPadding = false
//        )
//
//    ),
//)

val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),
    labelLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    ),

)

