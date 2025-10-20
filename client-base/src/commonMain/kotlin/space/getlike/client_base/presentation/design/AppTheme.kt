package space.getlike.client_base.presentation.design

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import space.getlike.util_core.LocalModalBottomSheetStyle
import space.getlike.util_core.ModalBottomSheetStyle
import space.getlike.util_core.Preview
import space.getlike.util_core.View
import space.getlike.util_core.utils.invoke

@Immutable
data class AppColors(
    val primary: Color = Color.Unspecified,
    val secondary: Color = Color.Unspecified,
    val tertiary: Color = Color.Unspecified,
    val accent: Color = Color.Unspecified,
    val background: Color = Color.Unspecified,
    val placeholder: Color = Color.Unspecified,
    val hint: Color = Color.Unspecified,
    val error: Color = Color.Unspecified,
)

@Immutable
data class AppTypography(
    val bodyHuge: TextStyle = TextStyle.Default,
    val bodyLarge: TextStyle = TextStyle.Default,
    val titleLarge: TextStyle = TextStyle.Default,
    val titleMedium: TextStyle = TextStyle.Default,
    val titleSmall: TextStyle = TextStyle.Default,
    val titleTiny: TextStyle = TextStyle.Default,
    val buttonLarge: TextStyle = TextStyle.Default,
    val buttonSmall: TextStyle = TextStyle.Default,
    val labelLarge: TextStyle = TextStyle.Default,
    val labelMedium: TextStyle = TextStyle.Default,
    val labelSmall: TextStyle = TextStyle.Default,
    val labelTiny: TextStyle = TextStyle.Default,
)

val LocalAppColors = staticCompositionLocalOf { AppColors() }

val LocalAppTypography = staticCompositionLocalOf { AppTypography() }

object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current
    val typography: AppTypography
        @Composable
        get() = LocalAppTypography.current
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalAppColors provides AppColors(
            primary = Color(0xFF5442B6),
            secondary = Color(0xFF2F2F2F),
            tertiary = Color(0xFFBFBEBF),
            accent = Color(0xFFFF7F5C),
            background = Color(0xFFFFFFFF),
            placeholder = Color(0xFFFFE18C),
            hint = Color(0xFFCFECFF),
            error = Color(0xFFEB5757),
        ),
        LocalAppTypography provides AppTypography(
            bodyHuge = TextStyle(fontSize = 60.sp, fontWeight = FontWeight.W800),
            bodyLarge = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.W800),
            titleLarge = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.W700),
            titleMedium = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.W700),
            titleSmall = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.W700),
            titleTiny = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.W500),
            buttonLarge = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.W600),
            buttonSmall = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W600),
            labelLarge = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.W400),
            labelMedium = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W500),
            labelSmall = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.W400),
            labelTiny = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.W400),

        ),
    ) {
        CompositionLocalProvider(
            LocalModalBottomSheetStyle provides ModalBottomSheetStyle(
                containerColor = AppTheme.colors.background,
                scrimColor = AppTheme.colors.secondary(0.7f),
                dragHandle = {
                    Box(
                        Modifier
                            .padding(16.dp)
                            .size(width = 32.dp, height = 4.dp)
                            .background(AppTheme.colors.secondary, shape = RoundedCornerShape(2.dp))
                            .clickable( // to remove DragHandle ripple
                                enabled = false,
                                onClick = { /* no op */ },
                            )
                        ,
                    )
                },
            ),
        ) {
            MaterialTheme(content = content)
        }
    }
}

@Composable
fun <TState : Any> ThemedPreview(
    view: (View.Bundle) -> View<*, TState>,
    state: () -> TState,
) {
    AppTheme { Preview(view, state) }
}

@Composable
fun <TState : Any> ThemedPreview(
    view: (View.Bundle) -> View<*, TState>,
    state: TState,
) {
    AppTheme { Preview(view, state) }
}