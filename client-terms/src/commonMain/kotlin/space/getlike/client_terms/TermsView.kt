package space.getlike.client_terms

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.unit.dp
import com.mikepenz.markdown.compose.Markdown
import com.mikepenz.markdown.model.DefaultMarkdownColors
import com.mikepenz.markdown.model.DefaultMarkdownTypography
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.client_base.presentation.design.atoms.DefaultIconButton
import space.getlike.client_base.presentation.design.atoms.DefaultScaffold
import space.getlike.client_base.presentation.design.atoms.DefaultScrollbar
import space.getlike.client_base.presentation.design.atoms.DefaultTopAppBar
import space.getlike.client_base.presentation.design.other.OffsetBehavior
import space.getlike.util_core.View
import space.getlike.resources.*

class TermsView(bundle: Bundle) : View<TermsViewModel, TermsState>(
    bundle = bundle,
    viewModelFactory = ::TermsViewModel,
) {

    @Composable
    override fun Ui() {
        val scrollState = rememberScrollState()
        DefaultScaffold(
            topBar = { TopBar(scrollState) },
            content = { paddingValues -> Content(paddingValues, scrollState) },
        )
    }

    @Composable
    private fun TopBar(scrollState: ScrollState) {
        DefaultTopAppBar(
            titleRes = Res.string.terms_title,
            offsetBehavior = OffsetBehavior.ScrollTop(scrollState),
            navigationIcon = {
                DefaultIconButton(
                    imageRes = Res.drawable.ic_back,
                    descriptionRes = Res.string.common_description_back,
                    onClick = { viewModel.onBackClick() },
                )
            },
        )
    }

    @Composable
    private fun Content(
        paddingValues: PaddingValues,
        scrollState: ScrollState,
    ) {
        DefaultScrollbar(
            modifier = Modifier
                .padding(paddingValues)
            ,
            state = scrollState,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (state.terms.isEmpty()) {
                    CircularProgressIndicator()
                } else {
                    Markdown(
                        modifier = Modifier
                            .padding(8.dp),
                        content = state.terms,
                        colors = DefaultMarkdownColors(
                            text = AppTheme.colors.secondary,
                            codeBackground = AppTheme.colors.secondary,
                            inlineCodeBackground = AppTheme.colors.secondary,
                            dividerColor = AppTheme.colors.secondary,
                            tableBackground = AppTheme.colors.secondary,
                        ),
                        typography = DefaultMarkdownTypography(
                            h1 = AppTheme.typography.titleLarge,
                            h2 = AppTheme.typography.titleMedium,
                            h3 = AppTheme.typography.titleSmall,
                            h4 = AppTheme.typography.labelMedium,
                            h5 = AppTheme.typography.labelMedium,
                            h6 = AppTheme.typography.labelMedium,
                            text = AppTheme.typography.labelMedium,
                            code = AppTheme.typography.labelMedium,
                            inlineCode = AppTheme.typography.labelMedium,
                            quote = AppTheme.typography.labelMedium,
                            paragraph = AppTheme.typography.labelMedium,
                            ordered = AppTheme.typography.labelMedium,
                            bullet = AppTheme.typography.labelMedium,
                            list = AppTheme.typography.labelMedium,
                            textLink = TextLinkStyles(),
                            table = AppTheme.typography.labelMedium,
                        ),
                    )
                }
            }
        }
    }
}