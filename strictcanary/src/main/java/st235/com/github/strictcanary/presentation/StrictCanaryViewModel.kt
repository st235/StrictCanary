package st235.com.github.strictcanary.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import st235.com.github.strictcanary.data.StrictCanaryViolation
import st235.com.github.strictcanary.data.StrictCanaryViolationsRepository
import st235.com.github.strictcanary.utils.ViolationsTree

internal sealed interface UiState {
    class ViolationsList(
        val treeNode: ViolationsTree.Node
    ) : UiState

    class DetailedViolation(
        val violation: StrictCanaryViolation,
        val violationNode: ViolationsTree.Node?
    ) : UiState
}

internal class StrictCanaryViewModel : ViewModel() {

    private val repository = StrictCanaryViolationsRepository.INSTANCE

    private val uiStateLiveData = MutableLiveData<UiState>()

    fun observeUiState(): LiveData<UiState> {
        return uiStateLiveData
    }

    fun resetState(violation: StrictCanaryViolation?) {
        uiStateLiveData.value = if (violation == null) {
            val violations = repository.snapshot
            val violationsTree = ViolationsTree.from(violations)

            UiState.ViolationsList(treeNode = violationsTree.rootNode)
        } else {
            UiState.DetailedViolation(violation, null)
        }
    }

    fun changeState(newNode: ViolationsTree.Node) {
        uiStateLiveData.value = if (newNode is ViolationsTree.Node.LeafNode) {
            UiState.DetailedViolation(
                violation = newNode.radixToken.value,
                violationNode = newNode
            )
        } else {
            UiState.ViolationsList(treeNode = newNode)
        }
    }

}
