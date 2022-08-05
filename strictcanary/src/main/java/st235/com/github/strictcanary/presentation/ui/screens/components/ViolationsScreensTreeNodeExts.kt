package st235.com.github.strictcanary.presentation.ui.screens.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import st235.com.github.strictcanary.R
import st235.com.github.strictcanary.data.description
import st235.com.github.strictcanary.utils.headline

internal val ViolationsScreensTree.Node?.skipParent: ViolationsScreensTree.Node?
get() {
    return this?.parentNode?.parentNode
}

@Composable
internal fun ViolationsScreensTree.Node?.localisedDescription(): String? {
    return when (val token = this?.radixToken) {
        is ViolationsScreensTree.RadixCompositionToken.Ownership ->
            if (token.is3rdParty) {
                stringResource(id = R.string.strict_canary_activity_list_other_violations_title)
            } else {
                stringResource(id = R.string.strict_canary_activity_list_my_violations_title)
            }
        is ViolationsScreensTree.RadixCompositionToken.StackTrace ->
            token.trace.split(".").last()
        is ViolationsScreensTree.RadixCompositionToken.Reference ->
            token.value.headline.description
        is ViolationsScreensTree.RadixCompositionToken.Root -> null
        else -> null
    }
}
