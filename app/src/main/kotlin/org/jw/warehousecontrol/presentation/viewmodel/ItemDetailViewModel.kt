package org.jw.warehousecontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jw.warehousecontrol.domain.repository.BorrowedItemsRepository
import org.jw.warehousecontrol.domain.repository.SavedItemsRepository
import org.jw.warehousecontrol.presentation.model.ItemModel
import org.jw.warehousecontrol.presentation.model.VolunteerModel
import org.jw.warehousecontrol.presentation.model.state.ItemDetailState
import org.jw.warehousecontrol.presentation.model.toEntity
import org.jw.warehousecontrol.presentation.model.toModel

/**
 * @author Ananda Camara
 */
internal class ItemDetailViewModel(
    private val borrowedItemsRepository: BorrowedItemsRepository,
    private val savedItemsRepository: SavedItemsRepository
) : ViewModel() {
    private val _stateFlow =
        MutableStateFlow<ItemDetailState>(ItemDetailState.None)

    val stateFlow: StateFlow<ItemDetailState>
        get() = _stateFlow

    fun getRegisteredVolunteers() = viewModelScope.launch {
        val volunteers = savedItemsRepository.getSavedVolunteers()
        _stateFlow.value = ItemDetailState.Success(volunteers.map { it.toModel() })
        _stateFlow.value = ItemDetailState.None
    }

    fun lendItem(item: ItemModel, volunteer: VolunteerModel) = viewModelScope.launch {
        borrowedItemsRepository.lendItems(item.toEntity(), to = volunteer.toEntity())
        _stateFlow.value = ItemDetailState.ShowSuccessMessage(LEND_ITEM_SUCCESS)
        _stateFlow.value = ItemDetailState.None
    }

    fun returnItem(item: ItemModel, volunteer: VolunteerModel) = viewModelScope.launch {
        borrowedItemsRepository.returnItem(item.toEntity(), volunteer.toEntity())
        _stateFlow.value = ItemDetailState.ShowSuccessMessage(RETURN_ITEM_SUCCESS)
        _stateFlow.value = ItemDetailState.None
    }

    companion object {
        private const val LEND_ITEM_SUCCESS = "Retirada de item registrada com sucesso"
        private const val RETURN_ITEM_SUCCESS = "Item devolvido com sucesso"
    }
}