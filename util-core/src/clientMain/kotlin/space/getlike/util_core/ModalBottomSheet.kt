package space.getlike.util_core

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalModalBottomSheetStyle = staticCompositionLocalOf<ModalBottomSheetStyle> {
    error("ModalBottomSheetStyle is not provided")
}

@Immutable
data class ModalBottomSheetStyle(
    internal val containerColor: Color,
    internal val scrimColor: Color,
    internal val dragHandle: @Composable (() -> Unit),
)

@Composable
fun ModalBottomSheet(
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val modalBottomSheetStyle = LocalModalBottomSheetStyle.current
    @OptIn(ExperimentalMaterial3Api::class)
    ModalBottomSheet(
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        onDismissRequest = onDismissRequest,
        containerColor = modalBottomSheetStyle.containerColor,
        scrimColor = modalBottomSheetStyle.scrimColor,
        dragHandle = modalBottomSheetStyle.dragHandle,
        content = content,
    )
}