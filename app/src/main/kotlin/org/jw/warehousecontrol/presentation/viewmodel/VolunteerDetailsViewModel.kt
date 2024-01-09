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
import org.jw.warehousecontrol.presentation.model.state.VolunteerDetailState
import org.jw.warehousecontrol.presentation.model.toEntity
import org.jw.warehousecontrol.presentation.model.toModel

/**
 * @author Ananda Camara
 */
internal class VolunteerDetailsViewModel(
    private val itemsRepository: BorrowedItemsRepository,
    private val savedItemsRepository: SavedItemsRepository
): ViewModel() {

    private val _stateFlow =
        MutableStateFlow<VolunteerDetailState>(VolunteerDetailState.None)

    val stateFlow: StateFlow<VolunteerDetailState>
        get() = _stateFlow

    fun getRegisteredItems() = viewModelScope.launch {
        val items = savedItemsRepository.getSavedItems()
        _stateFlow.value = VolunteerDetailState.Success(items.map { it.toModel() })
        _stateFlow.value = VolunteerDetailState.None
    }

    fun lendItem(item: ItemModel, volunteer: VolunteerModel) = viewModelScope.launch {
        itemsRepository.lendItems(item.toEntity(), to = volunteer.toEntity())
        _stateFlow.value = VolunteerDetailState.ShowSuccessMessage(LEND_ITEM_SUCCESS)
        _stateFlow.value = VolunteerDetailState.None
    }

    fun returnItem(item: ItemModel, volunteer: VolunteerModel) = viewModelScope.launch {
        itemsRepository.returnItem(item = item.toEntity(), from = volunteer.toEntity())
        _stateFlow.value = VolunteerDetailState.ShowSuccessMessage(RETURN_ITEM_SUCCESS)
        _stateFlow.value = VolunteerDetailState.None
    }

    companion object {
        private const val ITEMS_DATABASE_REFERENCE = "items"
        private const val LEND_ITEM_SUCCESS = "Retirada de item registrada com sucesso"
        private const val RETURN_ITEM_SUCCESS = "Item devolvido com sucesso"
    }
}