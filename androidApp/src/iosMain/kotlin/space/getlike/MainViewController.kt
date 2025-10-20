package space.getlike

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController
import space.getlike.client_base.dependencies.PlatformDependenciesFactory

fun MainViewController(): UIViewController {
    val platformDependenciesFactory = PlatformDependenciesFactory()
    val viewController = ComposeUIViewController {
        App(platformDependenciesFactory)
    }
    platformDependenciesFactory.viewController = viewController
    return viewController
}
