package st235.com.github.strictcanary.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import st235.com.github.strictcanary.R
import st235.com.github.strictcanary.data.StrictCanaryViolation
import st235.com.github.strictcanary.presentation.ui.screens.detailed.DetailedList
import st235.com.github.strictcanary.presentation.ui.screens.list.ViolationsClassGroup
import st235.com.github.strictcanary.presentation.ui.theme.StrictCanaryTheme
import st235.com.github.strictcanary.presentation.ui.localisedDescription

class StrictCanaryActivity : ComponentActivity() {

    companion object {

        private const val ARGS_KEY_VIOLATION = "args.violation"

        fun createIntent(
            context: Context,
            violation: StrictCanaryViolation? = null
        ): Intent {
            val intent = Intent(context, StrictCanaryActivity::class.java)
            if (violation != null) {
                intent.putExtra(ARGS_KEY_VIOLATION, violation)
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK and Intent.FLAG_ACTIVITY_CLEAR_TASK)
            return intent
        }

        private fun extractViolationFromIntent(intent: Intent): StrictCanaryViolation? {
            return intent.getParcelableExtra(ARGS_KEY_VIOLATION)
        }

    }

    private val viewModel by viewModels<StrictCanaryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val strictPolicyViolation = extractViolationFromIntent(intent)
        viewModel.resetState(strictPolicyViolation)

        setContent {
            StrictCanaryTheme {
                val uiState = viewModel.observeUiState().observeAsState()
                val uiStateValue = uiState.value

                val canGoUpper = when (uiStateValue) {
                    is UiState.DetailedViolation -> true
                    is UiState.ViolationsList -> uiStateValue.treeNode.parentNode != null
                    else -> false
                }

                val upperParent = when (uiStateValue) {
                    is UiState.DetailedViolation -> uiStateValue.violationNode?.parentNode?.parentNode
                    is UiState.ViolationsList -> uiStateValue.treeNode.parentNode?.parentNode
                    else -> null
                }

                Scaffold(
                    topBar = {
                        TopAppBar(
                            navigationIcon = {
                                if (canGoUpper) {
                                    Icon(
                                        imageVector = Icons.Rounded.ArrowBack,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .clickable {
                                                if (upperParent == null) {
                                                    viewModel.resetState(null)
                                                } else {
                                                    viewModel.changeState(upperParent)
                                                }
                                            }
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Rounded.Close,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .clickable {
                                                finish()
                                            }
                                    )
                                }
                            },
                            title = {
                                when (uiStateValue) {
                                    is UiState.DetailedViolation ->
                                        Text(
                                            text = stringResource(id = R.string.strict_canary_activity_title)
                                        )
                                    is UiState.ViolationsList ->
                                        Text(
                                            text = uiStateValue.treeNode.localisedDescription()
                                                ?: stringResource(id = R.string.strict_canary_activity_list_all),
                                            maxLines = 1
                                        )
                                    else ->
                                        Text(
                                            stringResource(id = R.string.strict_canary_activity_list_all)
                                        )
                                }
                            }
                        )
                    }
                ) { innerPaddings ->
                    RootView(
                        uiState = uiStateValue,
                        modifier = Modifier.padding(
                            bottom = innerPaddings.calculateBottomPadding()
                        )
                    )
                }
            }
        }
    }

    @Composable
    internal fun RootView(
        uiState: UiState?,
        modifier: Modifier = Modifier
    ) {
        when (uiState) {
            is UiState.ViolationsList ->
                ViolationsClassGroup(
                    treeNode = uiState.treeNode,
                    onViolationClickListener = { classGroup ->
                        viewModel.changeState(classGroup)
                    },
                    modifier = modifier
                )
            is UiState.DetailedViolation ->
                DetailedList(
                    strictCanaryViolation = uiState.violation,
                    modifier = modifier
                )
            else -> {
                // empty on purpose
            }
        }
    }
}
