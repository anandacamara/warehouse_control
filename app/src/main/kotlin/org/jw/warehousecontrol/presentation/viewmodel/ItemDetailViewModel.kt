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
import org.jw.warehousecontrol.presentation.model.state.ItemDetailState

/**
 * @author Ananda Camara
 */
internal class ItemDetailViewModel(
    private val lendItemsRepository: LendItemsRepository,
    private val returnItemRepository: ReturnItemRepository,
    private val savedItemsRepository: SavedItemsRepository
) : ViewModel() {
    private val _stateFlow =
        MutableStateFlow<ItemDetailState>(ItemDetailState.None)

    val stateFlow: StateFlow<ItemDetailState>
        get() = _stateFlow

    fun getRegisteredVolunteers() = viewModelScope.launch {
        val volunteers = savedItemsRepository.getSavedVolunteers()
        _stateFlow.value = ItemDetailState.Success(volunteers.map { it.toUIItem() })
        _stateFlow.value = ItemDetailState.None
    }

    fun lendItem(item: UIItem, volunteer: UIItemReference) = viewModelScope.launch {
        lendItemsRepository.lendItems(
            EntityReference(item.toItemEntity(), volunteer.count),
            to = volunteer.uiItem.toVolunteerEntity()
        )
        _stateFlow.value = ItemDetailState.ShowSuccessMessage(LEND_ITEM_SUCCESS)
        _stateFlow.value = ItemDetailState.None
    }

    fun returnItem(item: UIItem, volunteer: UIItem) = viewModelScope.launch {
        returnItemRepository.returnItem(
            EntityReference(item.toItemEntity(), 1),
            from = volunteer.toVolunteerEntity()
        )
        _stateFlow.value = ItemDetailState.ShowSuccessMessage(RETURN_ITEM_SUCCESS)
        _stateFlow.value = ItemDetailState.None
    }

    companion object {
        private const val LEND_ITEM_SUCCESS = "Retirada de item registrada com sucesso"
        private const val RETURN_ITEM_SUCCESS = "Item devolvido com sucesso"
    }
}