package org.jw.warehousecontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jw.warehousecontrol.domain.model.EntityReference
import org.jw.warehousecontrol.domain.repository.LendItemsRepository
import org.jw.warehousecontrol.domain.repository.SavedItemsRepository
import org.jw.warehousecontrol.presentation.model.*
import org.jw.warehousecontrol.presentation.model.state.LendItemState

/**
 * @author Ananda Camara
 */
internal class LendViewModel(
    private val lendItemsRepository: LendItemsRepository,
    private val savedItemsRepository: SavedItemsRepository
) : ViewModel() {

    private val _stateFlow =
        MutableStateFlow<LendItemState>(LendItemState.None)

    val stateFlow: StateFlow<LendItemState>
        get() = _stateFlow

    fun registerItems(volunteer: UIItem, items: List<UIItemReference>) = viewModelScope.launch {
        lendItemsRepository.lendItems(
            *items.map { EntityReference(it.uiItem.toItemEntity(), it.count) }.toTypedArray(),
            to = volunteer.toVolunteerEntity()
        )
        _stateFlow.value = LendItemState.RegisterItemsSuccess
        _stateFlow.value = LendItemState.None
    }

    fun getSavedItemsList() = viewModelScope.launch {
        val items = savedItemsRepository.getSavedItems()
        _stateFlow.value = LendItemState.ItemListSuccess(items.map { it.toUIItem() })
        _stateFlow.value = LendItemState.None
    }

    fun getSavedVolunteers() = viewModelScope.launch {
        val volunteers = savedItemsRepository.getSavedVolunteers()
        _stateFlow.value = LendItemState.VolunteerItemSuccess(volunteers.map { it.toUIItem() })
        _stateFlow.value = LendItemState.None
    }
}