package org.jw.warehousecontrol.presentation.model.state

import org.jw.warehousecontrol.presentation.model.ItemModel

sealed class VolunteerDetailState {
    data class Success(val items: List<ItemModel>): VolunteerDetailState()
    data class ShowSuccessMessage(val message: String): VolunteerDetailState()
    object Failure : VolunteerDetailState()
    object None : VolunteerDetailState()
}
