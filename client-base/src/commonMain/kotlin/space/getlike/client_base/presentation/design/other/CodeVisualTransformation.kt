package space.getlike.client_base.presentation.design.other

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

class CodeVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = text.text.filter { char -> char.isDigit() }
        val transformed = buildString {
            for (index in trimmed.indices) {
                if (index > 0 && index % CHAR_GROUP_LENGTH == 0) {
                    append(SEPARATOR)
                }
                append(trimmed[index])
            }
        }
        val mapping = object : OffsetMapping {

            override fun originalToTransformed(offset: Int): Int {
                val separators = (offset / CHAR_GROUP_LENGTH)
                    .coerceAtMost((trimmed.length - 1) / CHAR_GROUP_LENGTH)
                return offset + separators
            }

            override fun transformedToOriginal(offset: Int): Int {
                val raw = offset - (offset / (CHAR_GROUP_LENGTH + 1))
                return raw.coerceAtMost(trimmed.length)
            }
        }

        return TransformedText(
            text = androidx.compose.ui.text.AnnotatedString(transformed),
            offsetMapping = mapping,
        )
    }

    private companion object {
        const val CHAR_GROUP_LENGTH: Int = 3
        const val SEPARATOR: Char = ' '
    }
}