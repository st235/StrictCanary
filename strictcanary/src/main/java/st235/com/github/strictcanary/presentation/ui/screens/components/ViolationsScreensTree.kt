package st235.com.github.strictcanary.presentation.ui.screens.components

import st235.com.github.strictcanary.data.StrictCanaryViolation
import st235.com.github.strictcanary.utils.headline

internal class ViolationsScreensTree(
    violationsRadixDecompositions: List<Array<RadixCompositionToken>>
) {

    internal sealed interface RadixCompositionToken {

        object Root : RadixCompositionToken

        data class Ownership(
            val is3rdParty: Boolean
        ) : RadixCompositionToken

        data class StackTrace(
            val trace: String
        ) : RadixCompositionToken

        data class Reference(
            val value: StrictCanaryViolation
        ) : RadixCompositionToken

    }

    internal sealed interface Node {

        val radixToken: RadixCompositionToken

        val parentNode: Node?

        val size: Int

        val baselined: Boolean

        class InterimNode(
            override val parentNode: Node?,
            override val radixToken: RadixCompositionToken
        ) : Node, Iterable<Node> {

            private val children: MutableList<Node> = mutableListOf()

            override val size: Int by lazy {
                var value = 0

                for (child in children) {
                    value += child.size
                }

                return@lazy value
            }

            override val baselined: Boolean by lazy {
                var childrenBaselined = true

                for (child in this) {
                    childrenBaselined = childrenBaselined && child.baselined
                }

                childrenBaselined
            }

            override fun iterator(): Iterator<Node> {
                return children.iterator()
            }

            fun appendChild(child: Node) {
                children.add(child)
            }

        }

        class LeafNode(
            override val parentNode: Node?,
            override val radixToken: RadixCompositionToken.Reference
        ) : Node {

            override val size: Int = 1

            override val baselined: Boolean
                get() {
                    return radixToken.value.baselineType == StrictCanaryViolation.BaselineType.BASELINED
                }
        }

    }

    internal val rootNode: Node?

    init {
        rootNode = if (violationsRadixDecompositions.isNotEmpty()) {
            violationsRadixDecompositions.asTreeNode(
                parentNode = null,
                currentDepth = -1,
                radixValue = RadixCompositionToken.Root
            )
        } else {
            null
        }
    }

    private fun List<Array<RadixCompositionToken>>.asTreeNode(
        parentNode: Node?,
        currentDepth: Int,
        radixValue: RadixCompositionToken
    ): Node {
        val childrenDepth = currentDepth + 1
        val childrenGroups =
            mutableMapOf<RadixCompositionToken, MutableList<Array<RadixCompositionToken>>>()

        for (radixTokensArray in this) {
            if (childrenDepth >= radixTokensArray.size) {
                continue
            }

            val childRadix = radixTokensArray[childrenDepth]

            if (!childrenGroups.containsKey(childRadix)) {
                childrenGroups[childRadix] = mutableListOf()
            }

            childrenGroups.getValue(childRadix).add(radixTokensArray)
        }

        if (childrenGroups.isEmpty()) {
            if (this.size != 1) {
                throw IllegalStateException("Leaves should be in a singleton list but was found $this")
            }

            val rawLeaf = this.first()
            val referenceRadixToken = rawLeaf[currentDepth]

            if (referenceRadixToken !is RadixCompositionToken.Reference) {
                throw java.lang.IllegalStateException("Leaves should only be references")
            }

            return Node.LeafNode(
                parentNode = parentNode,
                radixToken = referenceRadixToken
            )
        }

        val interimNode = Node.InterimNode(
            parentNode = parentNode,
            radixToken = radixValue
        )

        for (groupEntity in childrenGroups) {
            val groupRadix = groupEntity.key
            val group = groupEntity.value

            interimNode.appendChild(
                child = group.asTreeNode(
                    parentNode = interimNode,
                    currentDepth = currentDepth + 1,
                    radixValue = groupRadix
                )
            )
        }

        return interimNode
    }

    companion object {

        fun from(violations: List<StrictCanaryViolation>): ViolationsScreensTree {
            return ViolationsScreensTree(
                violationsRadixDecompositions = violations.map { it.radixDecomposition }
            )
        }

        private val StrictCanaryViolation.radixDecomposition: Array<RadixCompositionToken>
            get() {
                val headline = this.headline

                return arrayOf(
                    RadixCompositionToken.Ownership(is3rdParty = !headline.isMyPackage),
                    RadixCompositionToken.StackTrace(trace = headline.className),
                    RadixCompositionToken.StackTrace(trace = headline.methodName),
                    RadixCompositionToken.Reference(value = this)
                )
            }

    }

}
