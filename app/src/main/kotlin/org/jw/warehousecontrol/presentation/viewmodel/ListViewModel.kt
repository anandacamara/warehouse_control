package org.jw.warehousecontrol.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jw.warehousecontrol.domain.repository.BorrowedItemsRepository
import org.jw.warehousecontrol.presentation.model.*
import org.jw.warehousecontrol.presentation.model.state.ListItemsState
import org.jw.warehousecontrol.presentation.model.toModel

/**
 * @author Ananda Camara
 */
internal class ListViewModel(
    private val repository: BorrowedItemsRepository
) : ViewModel() {

    private var listItemsModel: BorrowedItemsModel? = null

    private val _stateFlow =
        MutableStateFlow<ListItemsState>(ListItemsState.None)

    val stateFlow: StateFlow<ListItemsState>
        get() = _stateFlow

    fun getItems() = viewModelScope.launch {
        val items = repository.getBorrowedItems()
        listItemsModel = items.toModel()

        _stateFlow.value = ListItemsState.GetListItemsSuccess(
            itemsList = items.itemsDict.keys.map { it.toUIItem() },
            volunteersList = items.volunteersDict.keys.map { it.toUIItem() }
        )
    }

    fun getAssociatedVolunteers(uiItem: UIItem): List<UIItemReference> {
        return listItemsModel?.itemsDict?.get(uiItem) ?: listOf()
    }

    fun getAssociatedItems(uiItem: UIItem): List<UIItemReference> {
        return listItemsModel?.volunteersDict?.get(uiItem) ?: listOf()
    }
}