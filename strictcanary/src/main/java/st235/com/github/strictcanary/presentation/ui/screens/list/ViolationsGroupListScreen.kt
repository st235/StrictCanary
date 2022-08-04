package st235.com.github.strictcanary.presentation.ui.screens.list

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import st235.com.github.strictcanary.R
import st235.com.github.strictcanary.presentation.ui.screens.components.OnViolationClickListener
import st235.com.github.strictcanary.presentation.ui.screens.components.ViolationsGroup
import st235.com.github.strictcanary.presentation.ui.screens.components.ViolationsGroupList
import st235.com.github.strictcanary.presentation.ui.screens.components.ViolationsItem
import st235.com.github.strictcanary.presentation.ui.ViolationsScreensTree
import st235.com.github.strictcanary.utils.headline
import st235.com.github.strictcanary.presentation.ui.localisedDescription

@Composable
internal fun ViolationsClassGroup(
    treeNode: ViolationsScreensTree.Node,
    modifier: Modifier = Modifier,
    onViolationClickListener: OnViolationClickListener<ViolationsScreensTree.Node>? = null
) {
    if (treeNode !is ViolationsScreensTree.Node.InterimNode) {
        throw IllegalStateException("Only interim nodes can be used as first level pages nodes")
    }

    val groups = mutableListOf<ViolationsGroup<ViolationsScreensTree.Node>>()

    for (firstLevelChild in treeNode) {
        if (firstLevelChild !is ViolationsScreensTree.Node.InterimNode) {
            throw IllegalStateException("Only interim nodes can be used as second level pages nodes")
        }

        groups.add(
            ViolationsGroup(
                header = firstLevelChild.localisedDescription(),
                items = firstLevelChild.map { secondLevelChild ->
                    val header = if (secondLevelChild is ViolationsScreensTree.Node.LeafNode) {
                        val reference = secondLevelChild.radixToken.value
                        val headline = reference.headline
                        stringResource(id = R.string.strict_canary_activity_list_leaf_element_line, headline.line)
                    } else {
                        secondLevelChild.size.toString()
                    }

                    ViolationsItem(
                        counter = header,
                        content = secondLevelChild.localisedDescription() ?: "unkown",
                        item = secondLevelChild,
                        enabled = !secondLevelChild.baselined
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
