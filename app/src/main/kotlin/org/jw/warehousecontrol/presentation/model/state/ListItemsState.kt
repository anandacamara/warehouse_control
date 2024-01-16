package org.jw.warehousecontrol.presentation.model.state

import org.jw.warehousecontrol.presentation.model.ItemModel
import org.jw.warehousecontrol.presentation.model.UIItem
import org.jw.warehousecontrol.presentation.model.VolunteerModel

internal sealed class ListItemsState {
    data class GetListItemsSuccess(
        val itemsList: List<UIItem>, val volunteersList: List<UIItem>
    ) : ListItemsState()
    data class SaveListItemsSuccess(
        val successMessage: String
    ) : ListItemsState()

    object EmptyLocalListFailure : ListItemsState()
    object Failure : ListItemsState()
    object None : ListItemsState()
}
