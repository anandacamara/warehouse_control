package org.jw.warehousecontrol.presentation.model.state

import org.jw.warehousecontrol.presentation.model.UIItem

internal sealed class ItemDetailState {
    data class Success(val items: List<UIItem>): ItemDetailState()
    data class ShowSuccessMessage(val message: String) : ItemDetailState()
    object None : ItemDetailState()
}
