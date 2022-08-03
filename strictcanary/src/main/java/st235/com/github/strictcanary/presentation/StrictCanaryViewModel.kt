package st235.com.github.strictcanary.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import st235.com.github.strictcanary.data.StrictCanaryViolation
import st235.com.github.strictcanary.data.StrictCanaryViolationsRepository

internal sealed interface UiState {
    class ViolationsList(
        val violations: List<StrictCanaryViolation>
    ): UiState

    class DetailedViolation(
        val violation: StrictCanaryViolation
    ): UiState
}

internal class StrictCanaryViewModel: ViewModel() {

    private val repository = StrictCanaryViolationsRepository.INSTANCE

    private val uiStateLiveData = MutableLiveData<UiState>()

    fun observeUiState(): LiveData<UiState> {
        return uiStateLiveData
    }

    fun updateUiState(violation: StrictCanaryViolation?) {
        uiStateLiveData.value = if (violation == null) {
            val violationsList = repository.snapshot
             UiState.ViolationsList(violationsList)
        } else {
            UiState.DetailedViolation(violation)
        }
    }

}
