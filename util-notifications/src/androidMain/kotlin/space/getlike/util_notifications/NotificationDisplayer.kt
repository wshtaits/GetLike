package space.getlike.util_notifications

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat
import androidx.core.net.toUri
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import androidx.core.graphics.createBitmap

actual class NotificationDisplayer(
    private val context: Context,
) {

    private val notificationManager = NotificationManagerCompat.from(context)
    private val loader = ImageLoader(context)

    actual suspend fun showNotification(
        id: Int,
        smallImage: NotificationImage.Emoji,
        largeImage: NotificationImage?,
        title: String?,
        body: String?,
        colorInt: Int?,
        deeplink: String?,
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS,
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val builder = NotificationCompat.Builder(context, "default")

        builder.setAutoCancel(true)
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT)
        builder.setSmallIcon(getSmallIcon(smallImage))

        if (!title.isNullOrEmpty()) {
            builder.setContentTitle(title)
        }
        if (!body.isNullOrEmpty()) {
            builder.setContentText(body)
            builder.setStyle(NotificationCompat.BigTextStyle().bigText(body))
        }

        if (largeImage != null) {
            builder.setLargeIcon(getLargeIcon(largeImage))
        }

        if (colorInt != null) {
            builder.setColor(colorInt)
            builder.setColorized(true)
        }

        if (!deeplink.isNullOrEmpty()) {
            val intent = Intent(Intent.ACTION_VIEW)
                .apply {
                    setPackage(context.packageName)
                    data = deeplink.toUri()
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
            val pendingIntent = PendingIntent.getActivity(
                /* context = */ context,
                /* requestCode = */ 0,
                /* intent = */ intent,
                /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
            builder.setContentIntent(pendingIntent)
        }

        notificationManager.notify(id, builder.build())
    }

    actual fun hideNotification(id: Int) {
        notificationManager.cancel(id)
    }

    private fun getSmallIcon(image: NotificationImage.Emoji): IconCompat {
        val emojiBitmap = emojiToBitmap(emoji = image.value, sizePx = 64)
        return IconCompat.createWithBitmap(emojiBitmap)
    }

    private suspend fun getLargeIcon(image: NotificationImage?): Bitmap? =
        when (image) {
            is NotificationImage.Emoji ->
                emojiToBitmap(emoji = image.value, sizePx = 128)

            is NotificationImage.Uri -> {
                val request = ImageRequest.Builder(context)
                    .data(image.value)
                    .build()
                val result = loader.execute(request)
                if (result is SuccessResult) {
                    (result.drawable as? BitmapDrawable)?.bitmap
                } else {
                    null
                }
            }

            else -> null
        }

    private fun emojiToBitmap(emoji: String, sizePx: Int): Bitmap {
        val paint = Paint().apply {
            textSize = sizePx.toFloat()
            isAntiAlias = true
            textAlign = Paint.Align.CENTER
        }

        val baseline = -paint.ascent()
        val width = (paint.measureText(emoji) + 0.5f).toInt()
        val height = (baseline + paint.descent() + 0.5f).toInt()
        val bitmap = createBitmap(width, height)

        val canvas = Canvas(bitmap)
        canvas.drawText(emoji, width / 2f, baseline, paint)

        return bitmap
    }
}