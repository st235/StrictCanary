package st235.com.github.strictcanary.presentation.ui.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import st235.com.github.strictcanary.R

internal data class ViolationsGroup<T : Any>(
    val header: String?,
    val items: List<ViolationsItem<T>>
)

internal data class ViolationsItem<T>(
    val counter: String,
    val content: String,
    val item: T,
    val enabled: Boolean = true
)

internal typealias OnViolationClickListener<T> = (item: T) -> Unit

@Composable
internal fun <T : Any> ViolationsGroupList(
    groups: List<ViolationsGroup<T>>,
    modifier: Modifier = Modifier,
    onViolationClickListener: OnViolationClickListener<T>? = null
) {
    if (groups.isEmpty()) {
        EmptyBanner()

    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        for (group in groups) {
            val header = group.header

            if (header != null) {
                item {
                    Text(
                        text = header,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(top = 32.dp, bottom = 8.dp, start = 16.dp, end = 16.dp)
                    )
                }
            }

            val violationItems = group.items

            if (violationItems.isEmpty()) {
                item {
                    EmptyBanner()
                }
            } else {
                for (violationItem in violationItems) {
                    item {
                        CountedItemRowComponent(
                            violationsItem = violationItem,
                            onViolationClickListener = onViolationClickListener
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun <T : Any> CountedItemRowComponent(
    violationsItem: ViolationsItem<T>,
    onViolationClickListener: OnViolationClickListener<T>? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .fillMaxWidth()
            .clickable { onViolationClickListener?.invoke(violationsItem.item) }
            .padding(vertical = 8.dp)
    ) {
        val textStyle = if (violationsItem.enabled) {
            LocalTextStyle.current
        } else {
            TextStyle(textDecoration = TextDecoration.LineThrough)
        }

        Text(
            text = violationsItem.counter,
            color = if (violationsItem.enabled) MaterialTheme.colors.onSecondary else MaterialTheme.colors.onSurface,
            fontSize = 22.sp,
            style = textStyle,
            modifier = Modifier
                .padding(vertical = 2.dp, horizontal = 16.dp)
                .clip(CircleShape)
                .background(
                    if (violationsItem.enabled) MaterialTheme.colors.secondary else MaterialTheme.colors.surface
                )
                .padding(all = 10.dp)
        )
        Text(
            text = violationsItem.content,
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            style = textStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(end = 8.dp)
        )
    }
}

@Composable
internal fun EmptyBanner() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(R.drawable.ic_strtict_canary_filled_colorful),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.strict_canary_activity_list_empty_group),
            fontSize = 22.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(end = 8.dp)
        )
    }
}
