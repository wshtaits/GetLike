package space.getlike.client_base.presentation.design.molecules

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.createBitmap
import androidx.compose.ui.graphics.ImageBitmap

actual fun emojiToBitmap(emoji: String, sizePx: Int): ImageBitmap {
    val bitmap = createBitmap(sizePx, sizePx)
    val canvas = Canvas(bitmap)

    val paint = Paint().apply {
        isAntiAlias = true
        textAlign = Paint.Align.LEFT
    }

    val bounds = Rect()
    paint.textSize = 100f
    paint.getTextBounds(
        /* text = */ emoji,
        /* start = */ 0,
        /* end = */ emoji.length,
        /* bounds = */ bounds,
    )

    val scale = sizePx / bounds.height()
    paint.textSize = 100f * scale

    canvas.drawText(
        /* text = */ emoji,
        /* x = */ 0f,
        /* y = */ (sizePx / 2f) + (bounds.height() / 2f * scale) - bounds.bottom * scale,
        /* paint = */ paint,
    )

    return bitmap.asImageBitmap()
}