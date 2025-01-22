package dev.shtanko.androidlab.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import dev.shtanko.androidlab.R

val googleFontProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs,
)
val lexendDecaFont = GoogleFont("Lexend Deca")

val lexendDecaFontFamily = FontFamily(
    androidx.compose.ui.text.googlefonts.Font(
        googleFont = lexendDecaFont,
        fontProvider = googleFontProvider,
    ),
)

val typography = typographyFromDefaults(
    displayLarge = TextStyle(
        fontFamily = lexendDecaFontFamily,
        fontWeight = FontWeight.Bold,
    ),
    displayMedium = TextStyle(
        fontFamily = lexendDecaFontFamily,
        fontWeight = FontWeight.Bold,
    ),
    displaySmall = TextStyle(
        fontFamily = lexendDecaFontFamily,
        fontWeight = FontWeight.Bold,
    ),
    headlineLarge = TextStyle(
        fontFamily = lexendDecaFontFamily,
        fontWeight = FontWeight.Bold,
        lineHeight = 40.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = lexendDecaFontFamily,
        fontWeight = FontWeight.Bold,
    ),
    headlineSmall = TextStyle(
        fontFamily = lexendDecaFontFamily,
        fontWeight = FontWeight.W500,
        lineHeight = 28.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = lexendDecaFontFamily,
        fontWeight = FontWeight.W500,
        lineHeight = 22.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = lexendDecaFontFamily,
        fontWeight = FontWeight.W500,
    ),
    titleSmall = TextStyle(
        fontFamily = lexendDecaFontFamily,
        fontWeight = FontWeight.W400,
    ),
    bodyLarge = TextStyle(
        fontFamily = lexendDecaFontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 28.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = lexendDecaFontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = lexendDecaFontFamily,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = lexendDecaFontFamily,
        fontWeight = FontWeight.Bold,
    ),
    labelMedium = TextStyle(
        fontFamily = lexendDecaFontFamily,
    ),
    labelSmall = TextStyle(
        letterSpacing = 0.08.em,
    ),
)

@Suppress("CyclomaticComplexMethod", "LongParameterList")
fun typographyFromDefaults(
    displayLarge: TextStyle?,
    displayMedium: TextStyle?,
    displaySmall: TextStyle?,
    headlineLarge: TextStyle?,
    headlineMedium: TextStyle?,
    headlineSmall: TextStyle?,
    titleLarge: TextStyle?,
    titleMedium: TextStyle?,
    titleSmall: TextStyle?,
    bodyLarge: TextStyle?,
    bodyMedium: TextStyle?,
    bodySmall: TextStyle?,
    labelLarge: TextStyle?,
    labelMedium: TextStyle?,
    labelSmall: TextStyle?,
): Typography {
    val defaults = Typography()
    return Typography(
        displayLarge = displayLarge ?: defaults.displayLarge,
        displayMedium = displayMedium ?: defaults.displayMedium,
        displaySmall = displaySmall ?: defaults.displaySmall,
        headlineLarge = headlineLarge ?: defaults.headlineLarge,
        headlineMedium = headlineMedium ?: defaults.headlineMedium,
        headlineSmall = headlineSmall ?: defaults.headlineSmall,
        titleLarge = titleLarge ?: defaults.titleLarge,
        titleMedium = titleMedium ?: defaults.titleMedium,
        titleSmall = titleSmall ?: defaults.titleSmall,
        bodyLarge = bodyLarge ?: defaults.bodyLarge,
        bodyMedium = bodyMedium ?: defaults.bodyMedium,
        bodySmall = bodySmall ?: defaults.bodySmall,
        labelLarge = labelLarge ?: defaults.labelLarge,
        labelMedium = labelMedium ?: defaults.labelMedium,
        labelSmall = labelSmall ?: defaults.labelSmall,
    )
}
