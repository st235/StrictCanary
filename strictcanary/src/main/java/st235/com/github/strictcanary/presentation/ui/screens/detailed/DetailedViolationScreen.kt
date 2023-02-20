package st235.com.github.strictcanary.presentation.ui.screens.detailed

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Celebration
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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
import st235.com.github.strictcanary.data.description
import st235.com.github.strictcanary.data.hasMyPackageEntries
import st235.com.github.strictcanary.utils.ViolationClipBoardDescriptor
import st235.com.github.strictcanary.utils.clipboardDescriptor
import st235.com.github.strictcanary.utils.headline
import st235.com.github.strictcanary.utils.localisedTitleRes
import st235.com.github.strictcanary.utils.vectorIcon

@Composable
internal fun DetailedList(
    strictCanaryViolation: StrictCanaryViolation,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        ViolationTags(strictCanaryViolation)
        if (strictCanaryViolation.baselineType == StrictCanaryViolation.BaselineType.WHITELISTED) {
            ViolationClipboardSection(clipBoardDescriptor = strictCanaryViolation.clipboardDescriptor)
        }
        if (strictCanaryViolation.baselineMessage != null) {
            ViolationMessageSection(violationMessage = strictCanaryViolation.baselineMessage)
        }
        Text(
            text = stringResource(id = R.string.strict_canary_activity_stack_trace),
            fontSize = 24.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colors.onSurface,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp)
        )
        ViolationStackTraceBox(strictCanaryViolation)
    }
}

@Composable
internal fun ViolationMessageSection(violationMessage: String) {
    Text(
        text = stringResource(id = R.string.strict_canary_activity_message),
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp)
    )
    Text(
        text = violationMessage,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colors.surface)
            .padding(horizontal = 8.dp, vertical = 8.dp)
    )
}

@Composable
internal fun ViolationClipboardSection(clipBoardDescriptor: ViolationClipBoardDescriptor) {
    val context = LocalContext.current
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager

    Text(
        text = stringResource(id = R.string.strict_canary_activity_clipboard_info),
        fontSize = 24.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colors.onSurface,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp)
    )
    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colors.surface)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(10f)
        ) {
            Text(
                text = stringResource(
                    id = R.string.strict_canary_activity_clipboard_info_type,
                    clipBoardDescriptor.type
                ),
                fontSize = 18.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.onSurface,
            )
            Text(
                text = stringResource(
                    id = R.string.strict_canary_activity_clipboard_info_entry,
                    clipBoardDescriptor.entry
                ),
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        OutlinedButton(
            colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent),
            border = BorderStroke(0.dp, Color.Transparent),
            onClick = {
                val clipboardData =
                    ClipData.newPlainText(clipBoardDescriptor.type, clipBoardDescriptor.entry)
                clipboardManager?.setPrimaryClip(clipboardData)
                Toast
                    .makeText(
                        context,
                        context.getString(R.string.strict_canary_activity_clipboard_info_copied),
                        Toast.LENGTH_SHORT
                    )
                    .show()
            },
            modifier = Modifier
                .padding(all = 8.dp)
                .weight(3f)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Rounded.ContentCopy,
                    contentDescription = null,
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier
                        .width(24.dp)
                        .height(24.dp)
                )
                Text(
                    text = stringResource(R.string.strict_canary_activity_clipboard_info_copy),
                    fontSize = 12.sp,
                    maxLines = 1,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colors.onSurface,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
internal fun ViolationTags(strictCanaryViolation: StrictCanaryViolation) {
    FlowLayout(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, top = 16.dp)
    ) {
        ViolationTag(
            icon = strictCanaryViolation.type.vectorIcon,
            content = stringResource(id = strictCanaryViolation.type.localisedTitleRes),
            backgroundColor = MaterialTheme.colors.secondary,
            contentColor = MaterialTheme.colors.onSecondary,
            fontWeight = FontWeight.Bold
        )

        if (strictCanaryViolation.baselineType == StrictCanaryViolation.BaselineType.BASELINED) {
            ViolationTag(
                icon = Icons.Rounded.Close,
                content = stringResource(id = R.string.strict_canary_detailed_screen_tag_ignored),
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.onSurface,
                textStyle = TextStyle(textDecoration = TextDecoration.LineThrough)
            )
        }

        if (!strictCanaryViolation.hasMyPackageEntries) {
            ViolationTag(
                icon = Icons.Rounded.Celebration,
                content = stringResource(id = R.string.strict_canary_detailed_screen_tag_3rd_party),
                backgroundColor = MaterialTheme.colors.surface,
                contentColor = MaterialTheme.colors.onSurface
            )
        }

        ViolationTag(
            icon = null,
            content = strictCanaryViolation.headline.fileName
                ?: stringResource(id = R.string.strict_canary_detailed_screen_tag_unknown_source),
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
internal fun ViolationTag(
    icon: ImageVector?,
    content: String,
    backgroundColor: Color,
    contentColor: Color,
    fontWeight: FontWeight = FontWeight.Medium,
    textStyle: TextStyle = LocalTextStyle.current
) {
    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min)
            .padding(vertical = 2.dp, horizontal = 2.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(vertical = 4.dp)
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .fillMaxHeight()
            )
        }
        Text(
            text = content,
            fontSize = 16.sp,
            fontWeight = fontWeight,
            style = textStyle,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = contentColor,
            modifier = Modifier
                .padding(horizontal = 12.dp)
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
