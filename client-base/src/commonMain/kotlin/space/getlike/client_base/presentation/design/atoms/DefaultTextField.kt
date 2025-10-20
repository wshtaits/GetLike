package space.getlike.client_base.presentation.design.atoms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import space.getlike.client_base.presentation.design.AppTheme
import space.getlike.util_core.utils.invoke

@Composable
fun DefaultTextField(
    value: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    error: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Unspecified,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onValueChange: (String) -> Unit,
    onClick: () -> Unit
) {
    TextField(
        modifier = modifier
            .clickable { onClick() }
        ,
        value = value,
        enabled = enabled,
        textStyle = AppTheme.typography.titleMedium.copy(textAlign = TextAlign.Center),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
        ),
        visualTransformation = visualTransformation,
        singleLine = true,
        isError = error,
        colors = TextFieldDefaults.colors(
            focusedTextColor = AppTheme.colors.primary,
            unfocusedTextColor = AppTheme.colors.primary,
            disabledTextColor = AppTheme.colors.tertiary,
            errorTextColor = AppTheme.colors.primary,
            focusedContainerColor = AppTheme.colors.background,
            unfocusedContainerColor = AppTheme.colors.background,
            disabledContainerColor = AppTheme.colors.background,
            errorContainerColor = AppTheme.colors.background,
            focusedIndicatorColor = AppTheme.colors.primary(0.1f),
            unfocusedIndicatorColor = AppTheme.colors.primary(0.1f),
            errorIndicatorColor = AppTheme.colors.error,
            cursorColor = AppTheme.colors.primary,
            errorCursorColor = AppTheme.colors.primary,
        ),
        onValueChange = onValueChange,
        keyboardActions = keyboardActions,
    )
}
