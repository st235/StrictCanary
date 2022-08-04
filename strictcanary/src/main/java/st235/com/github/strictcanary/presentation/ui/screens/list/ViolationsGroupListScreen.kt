package st235.com.github.strictcanary.presentation.ui.screens.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import st235.com.github.strictcanary.R
import st235.com.github.strictcanary.presentation.ui.screens.OnViolationClickListener
import st235.com.github.strictcanary.presentation.ui.screens.ViolationsGroup
import st235.com.github.strictcanary.presentation.ui.screens.ViolationsGroupList
import st235.com.github.strictcanary.presentation.ui.screens.ViolationsItem
import st235.com.github.strictcanary.utils.ViolationsTree
import st235.com.github.strictcanary.utils.headline
import st235.com.github.strictcanary.utils.localisedDescription

@Composable
internal fun ViolationsClassGroup(
    treeNode: ViolationsTree.Node,
    modifier: Modifier = Modifier,
    onViolationClickListener: OnViolationClickListener<ViolationsTree.Node>? = null
) {
    if (treeNode !is ViolationsTree.Node.InterimNode) {
        throw IllegalStateException("Only interim nodes can be used as first level pages nodes")
    }

    val groups = mutableListOf<ViolationsGroup<ViolationsTree.Node>>()

    for (firstLevelChild in treeNode) {
        if (firstLevelChild !is ViolationsTree.Node.InterimNode) {
            throw IllegalStateException("Only interim nodes can be used as second level pages nodes")
        }

        groups.add(
            ViolationsGroup(
                header = firstLevelChild.localisedDescription(),
                items = firstLevelChild.map { secondLevelChild ->
                    val header = if (secondLevelChild is ViolationsTree.Node.LeafNode) {
                        val reference = secondLevelChild.radixToken.value
                        val headline = reference.headline
                        stringResource(id = R.string.strict_canary_activity_list_leaf_element_line, headline.line)
                    } else {
                        secondLevelChild.size.toString()
                    }

                    ViolationsItem(
                        counter = header,
                        content = secondLevelChild.localisedDescription() ?: "unkown",
                        item = secondLevelChild
                    )
                }
            )
        )
    }

    ViolationsGroupList(
        groups = groups,
        onViolationClickListener = onViolationClickListener,
        modifier = modifier
    )
}
