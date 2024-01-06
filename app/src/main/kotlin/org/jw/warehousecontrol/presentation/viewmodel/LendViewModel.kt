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
import org.jw.warehousecontrol.presentation.model.state.LendItemState
import org.jw.warehousecontrol.presentation.model.toEntity
import org.jw.warehousecontrol.presentation.model.toModel

/**
 * @author Ananda Camara
 */
internal class LendViewModel(
    private val borrowedItemsRepository: BorrowedItemsRepository,
    private val savedItemsRepository: SavedItemsRepository
) : ViewModel() {

    private val _stateFlow =
        MutableStateFlow<LendItemState>(LendItemState.None)

    val stateFlow: StateFlow<LendItemState>
        get() = _stateFlow

    fun registerItems(volunteer: VolunteerModel, items: List<ItemModel>) = viewModelScope.launch {
        borrowedItemsRepository.lendItems(
            *items.map { it.toEntity() }.toTypedArray(),
            to = volunteer.toEntity()
        )
        _stateFlow.value = LendItemState.RegisterItemsSuccess
        _stateFlow.value = LendItemState.None
    }

    fun getSavedItemsList() = viewModelScope.launch {
        val items = savedItemsRepository.getSavedItems()
        _stateFlow.value = LendItemState.ItemListSuccess(items.map { it.toModel() })
        _stateFlow.value = LendItemState.None
    }

    fun getSavedVolunteers() = viewModelScope.launch {
        val volunteers = savedItemsRepository.getSavedVolunteers()
        _stateFlow.value = LendItemState.VolunteerItemSuccess(volunteers.map { it.toModel() })
        _stateFlow.value = LendItemState.None
    }
}