package space.getlike.client_base.presentation.design.atoms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import space.getlike.client_base.presentation.design.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultAlertDialog(
    showing: Boolean,
    items: List<DefaultAlertDialogItem>,
    onDismissRequest: () -> Unit,
) {
    if (!showing) {
        return
    }

    BasicAlertDialog(
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            modifier = Modifier
                .wrapContentSize()
            ,
            color = AppTheme.colors.background,
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(modifier = Modifier.padding(vertical = 16.dp)) {
                for ((textRes, textColor, action) in items) {
                    Text(
                        modifier = Modifier
                            .clickable { action() }
                            .fillMaxWidth()
                            .padding(16.dp)
                        ,
                        text = stringResource(textRes),
                        style = AppTheme.typography.labelMedium,
                        color = textColor ?: AppTheme.colors.secondary,
                    )
                }
            }
        }
    }
}

data class DefaultAlertDialogItem(
    val textRes: StringResource,
    val textColor: Color? = null,
    val action: () -> Unit,
)