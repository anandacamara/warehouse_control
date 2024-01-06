package org.jw.warehousecontrol.presentation.model.state

import org.jw.warehousecontrol.presentation.model.ItemModel
import org.jw.warehousecontrol.presentation.model.VolunteerModel

internal sealed class LendItemState {
    object RegisterItemsSuccess : LendItemState()
    data class ItemListSuccess(val items: List<ItemModel>) : LendItemState()
    data class VolunteerItemSuccess(val volunteers: List<VolunteerModel>) : LendItemState()
    object None: LendItemState()
}