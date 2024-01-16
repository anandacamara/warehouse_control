package org.jw.warehousecontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jw.warehousecontrol.domain.model.EntityReference
import org.jw.warehousecontrol.domain.repository.LendItemsRepository
import org.jw.warehousecontrol.domain.repository.ReturnItemRepository
import org.jw.warehousecontrol.domain.repository.SavedItemsRepository
import org.jw.warehousecontrol.presentation.model.*
import org.jw.warehousecontrol.presentation.model.state.VolunteerDetailState

/**
 * @author Ananda Camara
 */
internal class VolunteerDetailsViewModel(
    private val lendItemsRepository: LendItemsRepository,
    private val returnItemRepository: ReturnItemRepository,
    private val savedItemsRepository: SavedItemsRepository
) : ViewModel() {

    private val _stateFlow =
        MutableStateFlow<VolunteerDetailState>(VolunteerDetailState.None)

    val stateFlow: StateFlow<VolunteerDetailState>
        get() = _stateFlow

    fun getRegisteredItems() = viewModelScope.launch {
        val items = savedItemsRepository.getSavedItems()
        _stateFlow.value = VolunteerDetailState.Success(items.map { it.toUIItem() })
        _stateFlow.value = VolunteerDetailState.None
    }

    fun lendItem(item: UIItemReference, volunteer: UIItem) = viewModelScope.launch {
        lendItemsRepository.lendItems(
            EntityReference(item.uiItem.toItemEntity(), item.count),
            to = volunteer.toVolunteerEntity()
        )
        _stateFlow.value = VolunteerDetailState.ShowSuccessMessage(LEND_ITEM_SUCCESS)
        _stateFlow.value = VolunteerDetailState.None
    }

    fun returnItem(item: UIItem, volunteer: UIItem) = viewModelScope.launch {
        returnItemRepository.returnItem(
            item = EntityReference(item.toItemEntity(), 1),
            from = volunteer.toVolunteerEntity()
        )
        _stateFlow.value = VolunteerDetailState.ShowSuccessMessage(RETURN_ITEM_SUCCESS)
        _stateFlow.value = VolunteerDetailState.None
    }

    companion object {
        private const val LEND_ITEM_SUCCESS = "Retirada de item registrada com sucesso"
        private const val RETURN_ITEM_SUCCESS = "Item devolvido com sucesso"
    }
}