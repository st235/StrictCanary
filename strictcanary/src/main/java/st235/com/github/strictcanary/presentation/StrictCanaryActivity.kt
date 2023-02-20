package st235.com.github.strictcanary.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import st235.com.github.strictcanary.R
import st235.com.github.strictcanary.data.StrictCanaryViolation
import st235.com.github.strictcanary.presentation.ui.screens.components.ViolationsScreensTree
import st235.com.github.strictcanary.presentation.ui.screens.detailed.DetailedList
import st235.com.github.strictcanary.presentation.ui.screens.list.ViolationsTreeList
import st235.com.github.strictcanary.presentation.ui.theme.StrictCanaryTheme
import st235.com.github.strictcanary.presentation.ui.screens.components.localisedDescription
import st235.com.github.strictcanary.presentation.ui.screens.components.skipParent

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

    internal data class TopBarState(
        val content: String,
        val canGoUpper: Boolean,
        // we are using skip parents
        // as we are showing 3 nodes (skipParent -> parent -> node)
        // on the screen
        val skipParent: ViolationsScreensTree.Node?
    )

    private val viewModel by viewModels<StrictCanaryViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val strictPolicyViolation = extractViolationFromIntent(intent)
        viewModel.resetState(strictPolicyViolation)

        setContent {
            StrictCanaryTheme {
                val uiState = viewModel.observeUiState().observeAsState()
                val uiStateValue = uiState.value

                val topBarState = uiStateValue.asTopBarState()

                Scaffold(
                    topBar = {
                        TopAppBar(
                            navigationIcon = {
                                if (topBarState.canGoUpper) {
                                    Icon(
                                        imageVector = Icons.Rounded.ArrowBack,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = rememberRipple(bounded = false),
                                                onClick = { viewModel.changeState(topBarState.skipParent) }
                                            )
                                            .padding(16.dp)
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Rounded.Close,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .clickable(
                                                interactionSource = remember { MutableInteractionSource() },
                                                indication = rememberRipple(bounded = false),
                                                onClick = { finish() }
                                            )
                                            .padding(16.dp)
                                    )
                                }
                            },
                            title = {
                                Text(
                                    text = topBarState.content,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
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
    private fun UiState?.asTopBarState(): TopBarState {
        return when (this) {
            is UiState.DetailedViolation ->
                TopBarState(
                    content = stringResource(id = R.string.strict_canary_activity_title),
                    canGoUpper = true,
                    skipParent = this.violationNode.skipParent
                )
            is UiState.ViolationsList ->
                TopBarState(
                    content = this.screenNode.localisedDescription()
                        ?: stringResource(id = R.string.strict_canary_activity_list_all),
                    canGoUpper = this.screenNode?.skipParent != null,
                    skipParent = this.screenNode?.skipParent
                )
            else ->
                TopBarState(
                    content = stringResource(id = R.string.strict_canary_activity_list_all),
                    canGoUpper = false,
                    skipParent = null
                )
        }
    }

    @Composable
    private fun RootView(
        uiState: UiState?,
        modifier: Modifier = Modifier
    ) {
        when (uiState) {
            is UiState.ViolationsList ->
                ViolationsTreeList(
                    treeNode = uiState.screenNode,
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
