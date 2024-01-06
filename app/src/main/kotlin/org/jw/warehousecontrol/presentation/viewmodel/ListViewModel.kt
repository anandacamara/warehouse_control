package org.jw.warehousecontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jw.warehousecontrol.domain.repository.BorrowedItemsRepository
import org.jw.warehousecontrol.presentation.model.ItemModel
import org.jw.warehousecontrol.presentation.model.ListItemsModel
import org.jw.warehousecontrol.presentation.model.VolunteerModel
import org.jw.warehousecontrol.presentation.model.state.ListItemsState
import org.jw.warehousecontrol.presentation.model.toModel

/**
 * @author Ananda Camara
 */
internal class ListViewModel(
    private val repository: BorrowedItemsRepository
) : ViewModel() {

    private var listItemsModel: ListItemsModel? = null

    private val _stateFlow =
        MutableStateFlow<ListItemsState>(ListItemsState.None)

    val stateFlow: StateFlow<ListItemsState>
        get() = _stateFlow

    fun getItems() = viewModelScope.launch {
        val items = repository.getBorrowedItems()
        listItemsModel = items.toModel()

        _stateFlow.value = ListItemsState.GetListItemsSuccess(
            itemsList = items.itemsDict.keys.map { it.toModel() },
            volunteersList = items.volunteersDict.keys.map { it.toModel() }
        )
    }

    fun getAssociatedVolunteers(item: ItemModel): List<VolunteerModel> {
        return listItemsModel?.itemsDict?.get(item) ?: listOf()
    }

    fun getAssociatedItems(volunteer: VolunteerModel): List<ItemModel> {
        return listItemsModel?.volunteersDict?.get(volunteer) ?: listOf()
    }
}