package space.getlike.client_base.presentation.utils

import androidx.compose.runtime.Composable
import kotlin.time.Clock
import kotlin.time.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import space.getlike.resources.*

@Composable
fun Instant.toTimePassedString(): String {
    val localDateTime = toLocalDateTime(TimeZone.currentSystemDefault())
    val daysUntil = daysUntil(Clock.System.now(), TimeZone.currentSystemDefault())
    return when (daysUntil) {
        0 -> {
            localDateTime.format(
                LocalDateTime.Format {
                    hour()
                    char(':')
                    minute()
                }
            )
        }
        1 -> stringResource(Res.string.day_yesterday)
        else -> {
            val monthNames = MonthNames(
                january = stringResource(Res.string.month_january_short),
                february = stringResource(Res.string.month_february_short),
                march = stringResource(Res.string.month_march_short),
                april = stringResource(Res.string.month_april_short),
                may = stringResource(Res.string.month_may_short),
                june = stringResource(Res.string.month_june_short),
                july = stringResource(Res.string.month_july_short),
                august = stringResource(Res.string.month_august_short),
                september = stringResource(Res.string.month_september_short),
                october = stringResource(Res.string.month_october_short),
                november = stringResource(Res.string.month_november_short),
                december = stringResource(Res.string.month_december_short),
            )
            localDateTime.format(
                LocalDateTime.Format {
                    day(Padding.NONE)
                    char(' ')
                    monthName(monthNames)
                }
            )
        }
    }
}