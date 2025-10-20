package space.getlike.client_splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.client_base.presentation.design.atoms.DefaultAnimatedVisibility
import space.getlike.client_base.presentation.design.atoms.DefaultScaffold
import space.getlike.client_base.presentation.design.atoms.DefaultSpacer
import space.getlike.client_base.presentation.design.other.styledStringArrayResource
import space.getlike.util_core.View
import space.getlike.resources.*
import space.getlike.util_core.utils.invoke
import space.getlike.util_core.utils.rememberSaveableMutableStateOf

class SplashView(bundle: Bundle) : View<SplashViewModel, SplashState>(
    bundle = bundle,
    viewModelFactory = ::SplashViewModel,
) {

    @Composable
    override fun Ui() {
        DefaultScaffold(
            content = { Content() }
        )
    }

    @Composable
    private fun Content() {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
            ,
        ) {
            val (icon, label) = createRefs()
            val isLabelVisible = rememberSaveableMutableStateOf(false)

            LaunchedEffect(Unit) {
                isLabelVisible.value = true
            }

            Image(
                modifier = Modifier
                    .background(
                        color = AppTheme.colors.placeholder,
                        shape = RoundedCornerShape(52.dp),
                    )
                    .size(140.dp)
                    .padding(38.dp)
                    .constrainAs(icon) {
                        centerTo(parent)
                    }
                ,
                painter = painterResource(Res.drawable.img_splash),
                contentDescription = stringResource(Res.string.splash_icon),
            )

            DefaultAnimatedVisibility(
                modifier = Modifier
                    .constrainAs(label) {
                        top.linkTo(icon.bottom)
                        centerHorizontallyTo(parent)
                    }
                ,
                visible = isLabelVisible.value,
                duration = 250,
                fade = true,
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    DefaultSpacer(height = 42.dp)
                    Text(
                        text = stringResource(Res.string.splash_title),
                        color = AppTheme.colors.secondary,
                        style = AppTheme.typography.titleLarge,
                    )
                    DefaultSpacer(height = 8.dp)
                    Text(
                        text = styledStringArrayResource(
                            res = Res.array.splash_subtitle,
                            style = SpanStyle(fontWeight = FontWeight.W600),
                        ),
                        color = AppTheme.colors.secondary(0.7f),
                        style = AppTheme.typography.labelLarge,
                    )
                    DefaultSpacer(height = 40.dp)
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(24.dp)
                        ,
                        color = AppTheme.colors.primary,
                    )
                }
            }
        }
    }
}