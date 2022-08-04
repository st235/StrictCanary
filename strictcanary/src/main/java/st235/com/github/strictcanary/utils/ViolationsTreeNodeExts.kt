package st235.com.github.strictcanary.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import st235.com.github.strictcanary.R
import st235.com.github.strictcanary.data.description

@Composable
internal fun ViolationsTree.Node.localisedDescription(): String? {
    return when (val token = this.radixToken) {
        is ViolationsTree.RadixCompositionToken.Root -> null
        is ViolationsTree.RadixCompositionToken.Ownership ->
            if (token.is3rdParty) {
                stringResource(id = R.string.strict_canary_activity_list_other_violations_title)
            } else {
                stringResource(id = R.string.strict_canary_activity_list_my_violations_title)
            }
        is ViolationsTree.RadixCompositionToken.StackTrace ->
            token.trace.split(".").last()
        is ViolationsTree.RadixCompositionToken.Reference ->
            token.value.headline.description
    }
}
