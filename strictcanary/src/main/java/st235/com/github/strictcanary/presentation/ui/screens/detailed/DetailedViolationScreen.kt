package st235.com.github.strictcanary.presentation.ui.screens.detailed

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import st235.com.github.flowlayout.compose.FlowLayout
import st235.com.github.strictcanary.R
import st235.com.github.strictcanary.data.StrictCanaryViolation
import st235.com.github.strictcanary.data.StrictCanaryViolationEntry
import st235.com.github.strictcanary.data.baselineType
import st235.com.github.strictcanary.data.description
import st235.com.github.strictcanary.utils.localisedTitleRes
import st235.com.github.strictcanary.utils.vectorIcon

@Composable
internal fun DetailedList(
    strictCanaryViolation: StrictCanaryViolation,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        ViolationTags(strictCanaryViolation)
        Text(
            text = stringResource(id = R.string.strict_canary_activity_stack_trace),
            fontSize = 26.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
        )
        ViolationStackTraceBox(strictCanaryViolation)
    }
}

@Composable
internal fun ViolationTags(strictCanaryViolation: StrictCanaryViolation) {
    FlowLayout(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 16.dp)
    ) {
        ViolationType(strictCanaryViolation)
        if (strictCanaryViolation.baselineType == StrictCanaryViolation.BaselineType.BASELINED) {
            ViolationBaselined(strictCanaryViolation)
        }
        ViolationSourceTag(strictCanaryViolation)
    }
}

@Composable
internal fun ViolationType(strictCanaryViolation: StrictCanaryViolation) {
    val type = strictCanaryViolation.type

    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(vertical = 2.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colors.secondary)
            .padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = type.vectorIcon,
            contentDescription = null,
            tint = MaterialTheme.colors.onSecondary,
            modifier = Modifier
                .padding(start = 12.dp)
                .fillMaxHeight()
        )
        Text(
            text = stringResource(id = type.localisedTitleRes),
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colors.onSecondary,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )
    }
}

@Composable
internal fun ViolationBaselined(strictCanaryViolation: StrictCanaryViolation) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(vertical = 2.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colors.surface)
            .padding(vertical = 4.dp)
    ) {
        Icon(
            imageVector = Icons.Rounded.Close,
            contentDescription = null,
            tint = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .padding(start = 12.dp)
                .fillMaxHeight()
        )
        Text(
            text = stringResource(id = R.string.strict_canary_detailed_screen_tag_ignored),
            fontSize = 22.sp,
            fontWeight = FontWeight.Medium,
            style = TextStyle(textDecoration = TextDecoration.LineThrough),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )
    }
}

@Composable
internal fun ViolationSourceTag(strictCanaryViolation: StrictCanaryViolation) {
    val topEntry =
        strictCanaryViolation.violationEntriesStack.getOrNull(strictCanaryViolation.myPackageOffset)

    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(vertical = 2.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colors.surface)
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = topEntry?.fileName ?: "unknown",
            fontSize = 22.sp,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .padding(horizontal = 8.dp)
        )
    }
}

@Composable
internal fun ViolationStackTraceBox(strictCanaryViolation: StrictCanaryViolation) {
    val verticalScrollState = rememberLazyListState()
    val horizontalScrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colors.surface)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = verticalScrollState,
            modifier = Modifier
                .horizontalScroll(horizontalScrollState)
        ) {
            for (entry in strictCanaryViolation.violationEntriesStack) {
                val entryId = entry.hashCode()
                item(key = entryId) { ViolationStackTraceRow(entry) }
            }
        }
    }
}

@Composable
internal fun ViolationStackTraceRow(strictPolicyViolationEntry: StrictCanaryViolationEntry) {
    Row(
        modifier = Modifier.height(IntrinsicSize.Min)
    ) {
        val isMyPackageEntry =
            strictPolicyViolationEntry.isMyPackage

        if (isMyPackageEntry) {
            Icon(
                painter = painterResource(id = R.drawable.ic_strict_canary_small_outline),
                contentDescription = null,
                tint = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .fillMaxHeight()
            )
        }

        Text(
            text = strictPolicyViolationEntry.description,
            fontSize = 16.sp,
            fontWeight = if (isMyPackageEntry) FontWeight.Medium else FontWeight.Normal,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}
