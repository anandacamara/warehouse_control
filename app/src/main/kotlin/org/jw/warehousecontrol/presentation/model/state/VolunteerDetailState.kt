package org.jw.warehousecontrol.presentation.model.state

import org.jw.warehousecontrol.presentation.model.UIItem

sealed class VolunteerDetailState {
    data class Success(val items: List<UIItem>): VolunteerDetailState()
    data class ShowSuccessMessage(val message: String): VolunteerDetailState()
    object Failure : VolunteerDetailState()
    object None : VolunteerDetailState()
}
