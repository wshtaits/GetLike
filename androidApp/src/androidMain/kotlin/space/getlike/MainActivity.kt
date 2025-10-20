package space.getlike

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import space.getlike.client_base.dependencies.PlatformDependenciesFactory
import space.getlike.util_deeplinks.DeeplinkHandler

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        handleDeeplink(intent)
        setContent {
            enableEdgeToEdge()
            val context = LocalContext.current.applicationContext
            val activity = LocalActivity.current!! as ComponentActivity
            val platformDependencies = remember { PlatformDependenciesFactory(context, activity) }
            App(platformDependencies)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeeplink(intent)
    }

    private fun handleDeeplink(intent: Intent?) =
        intent?.data
            ?.toString()
            ?.let(DeeplinkHandler::handle)
}