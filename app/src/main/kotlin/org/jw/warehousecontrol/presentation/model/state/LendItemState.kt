package org.jw.warehousecontrol.presentation.model.state

import org.jw.warehousecontrol.presentation.model.ItemModel
import org.jw.warehousecontrol.presentation.model.UIItem
import org.jw.warehousecontrol.presentation.model.VolunteerModel

internal sealed class LendItemState {
    object RegisterItemsSuccess : LendItemState()
    data class ItemListSuccess(val items: List<UIItem>) : LendItemState()
    data class VolunteerItemSuccess(val volunteers: List<UIItem>) : LendItemState()
    object None: LendItemState()
}