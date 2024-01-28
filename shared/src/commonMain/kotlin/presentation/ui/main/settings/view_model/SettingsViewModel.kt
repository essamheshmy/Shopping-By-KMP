package presentation.ui.main.settings.view_model

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import business.core.DataState
import business.core.NetworkState
import business.core.Queue
import business.core.UIComponent
import business.core.UIComponentState
import business.interactors.main.AddAddressInteractor
import business.interactors.main.GetAddressesInteractor
import business.interactors.main.LogoutInteractor
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import presentation.ui.main.search.view_model.SearchEvent

class SettingsViewModel(
    private val logoutInteractor: LogoutInteractor,
) : ViewModel() {


    private val TAG = "AppDebug SettingsViewModel"


    val state: MutableState<SettingsState> = mutableStateOf(SettingsState())


    fun onTriggerEvent(event: SettingsEvent) {
        when (event) {

 
            is SettingsEvent.Logout -> {
                logout()
            }

            is SettingsEvent.OnRemoveHeadFromQueue -> {
                removeHeadMessage()
            }

            is SettingsEvent.Error -> {
                appendToMessageQueue(event.uiComponent)
            }

            is SettingsEvent.OnRetryNetwork -> {
                onRetryNetwork()
            }

            is SettingsEvent.OnUpdateNetworkState -> {
                onUpdateNetworkState(event.networkState)
            }
        }
    }

    private fun logout() {

        logoutInteractor.execute()
            .onEach { dataState ->
                when (dataState) {
                    is DataState.NetworkStatus -> {}
                    is DataState.Response -> {
                        onTriggerEvent(SettingsEvent.Error(dataState.uiComponent))
                    }

                    is DataState.Data -> {
                        dataState.data?.let {
                            state.value = state.value.copy(logout = it)

                        }
                    }

                    is DataState.Loading -> {
                        state.value =
                            state.value.copy(progressBarState = dataState.progressBarState)
                    }
                }
            }.launchIn(viewModelScope)
    }

    private fun appendToMessageQueue(uiComponent: UIComponent) {
        if (uiComponent is UIComponent.None) {
            println("${TAG}: onTriggerEvent:  ${(uiComponent as UIComponent.None).message}")
            return
        }

        val queue = state.value.errorQueue
        queue.add(uiComponent)
        state.value = state.value.copy(errorQueue = Queue(mutableListOf())) // force recompose
        state.value = state.value.copy(errorQueue = queue)
    }

    private fun removeHeadMessage() {
        try {
            val queue = state.value.errorQueue
            queue.remove() // can throw exception if empty
            state.value = state.value.copy(errorQueue = Queue(mutableListOf())) // force recompose
            state.value = state.value.copy(errorQueue = queue)
        } catch (e: Exception) {
            println("${TAG}: removeHeadMessage: Nothing to remove from DialogQueue")
        }
    }


    private fun onRetryNetwork() {

    }


    private fun onUpdateNetworkState(networkState: NetworkState) {
        state.value = state.value.copy(networkState = networkState)
    }


}