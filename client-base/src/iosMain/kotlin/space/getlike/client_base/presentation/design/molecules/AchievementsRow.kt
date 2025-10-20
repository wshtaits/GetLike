package space.getlike.client_base.presentation.design.molecules

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.*

actual fun emojiToBitmap(emoji: String, sizePx: Int): ImageBitmap {
    val surface = Surface.makeRasterN32Premul(sizePx, sizePx)
    val paint = Paint().apply { isAntiAlias = true }
    val font = Font(Typeface.makeEmpty(), sizePx.toFloat() * 0.8f)
    val bounds = font.measureText(emoji, paint)
    surface.canvas.drawString(
        s = emoji,
        x = (sizePx - bounds.width) / 2f - bounds.left,
        y = (sizePx + bounds.height) / 2f - bounds.bottom,
        font = font,
        paint = paint,
    )
    return surface.makeImageSnapshot().toComposeImageBitmap()
}
