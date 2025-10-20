package space.getlike.client_base.presentation.design.other

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import org.jetbrains.compose.resources.StringArrayResource
import org.jetbrains.compose.resources.stringArrayResource

@Composable
fun styledStringArrayResource(
    res: StringArrayResource,
    style: SpanStyle,
): AnnotatedString =
    buildAnnotatedString {
        val stringArray = stringArrayResource(res)
        append(stringArray[0])
        append(AnnotatedString(stringArray[1], style))
        append(stringArray[2])
    }