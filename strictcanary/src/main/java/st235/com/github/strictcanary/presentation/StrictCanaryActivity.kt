package st235.com.github.strictcanary.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import st235.com.github.strictcanary.R
import st235.com.github.strictcanary.data.StrictPolicyViolation
import st235.com.github.strictcanary.data.StrictPolicyViolationEntry
import st235.com.github.strictcanary.data.description
import st235.com.github.strictcanary.data.isMyPackage
import st235.com.github.strictcanary.presentation.ui.theme.StrictCanaryTheme
import st235.com.github.strictcanary.utils.localisedTitleRes
import st235.com.github.strictcanary.utils.vectorIcon

class StrictCanaryActivity : ComponentActivity() {

    companion object {

        private const val ARGS_KEY_VIOLATION = "args.violation"

        fun createIntent(context: Context, strictPolicyViolation: StrictPolicyViolation): Intent {
            val intent = Intent(context, StrictCanaryActivity::class.java)
            intent.putExtra(ARGS_KEY_VIOLATION, strictPolicyViolation)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK)
            return intent
        }

        private fun extractViolationFromIntent(intent: Intent): StrictPolicyViolation {
            return intent.getParcelableExtra(ARGS_KEY_VIOLATION)
                ?: throw IllegalStateException("This activity cannot be started without violation info")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val strictPolicyViolation = extractViolationFromIntent(intent)

        setContent {
            StrictCanaryTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text(text = stringResource(id = R.string.strict_canary_activity_title)) }
                        )
                    }
                ) {
                    RootView(strictPolicyViolation)
                }
            }
        }
    }

    @Composable
    internal fun RootView(strictPolicyViolation: StrictPolicyViolation) {
        Column {
            ViolationType(strictPolicyViolation)
            Text(
                text = stringResource(id = R.string.strict_canary_activity_stack_trace),
                fontSize = 26.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colors.onSurface,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp)
            )
            ViolationStackTraceBox(strictPolicyViolation)
        }
    }

    @Composable
    internal fun ViolationType(strictPolicyViolation: StrictPolicyViolation) {
        val type = strictPolicyViolation.type

        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(horizontal = 8.dp, vertical = 16.dp)
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
                color = MaterialTheme.colors.onSecondary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
            )
        }
    }

    @Composable
    internal fun ViolationStackTraceBox(strictPolicyViolation: StrictPolicyViolation) {
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
                for (entry in strictPolicyViolation.violationEntriesStack) {
                    val entryId = entry.hashCode()
                    item(key = entryId) { ViolationStackTraceRow(entry) }
                }
            }
        }
    }

    @Composable
    internal fun ViolationStackTraceRow(strictPolicyViolationEntry: StrictPolicyViolationEntry) {
        Row(
            modifier = Modifier.height(IntrinsicSize.Min)
        ) {
            val isMyPackageEntry = strictPolicyViolationEntry.isMyPackage(applicationContext)

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
}
