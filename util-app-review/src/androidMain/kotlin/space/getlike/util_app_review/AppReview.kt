package space.getlike.util_app_review

import android.app.Activity
import android.content.Intent
import com.google.android.play.core.review.ReviewManagerFactory
import androidx.core.net.toUri

actual class AppReview(
    private val activity: Activity,
) {

    actual fun launch() {
        val reviewManager = ReviewManagerFactory.create(activity)
        val request = reviewManager.requestReviewFlow()
        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                reviewManager.launchReviewFlow(activity, reviewInfo)
            } else {
                val intent = Intent(Intent.ACTION_VIEW)
                    .apply {
                        data = "https://play.google.com/store/apps/details?id=${activity.packageName}".toUri()
                        setPackage("com.android.vending")
                    }
                activity.startActivity(intent)
            }
        }
    }
}