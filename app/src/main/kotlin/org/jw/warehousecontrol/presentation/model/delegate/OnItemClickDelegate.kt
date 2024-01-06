package org.jw.warehousecontrol.presentation.model.delegate

import org.jw.warehousecontrol.presentation.model.ItemModel
import org.jw.warehousecontrol.presentation.model.VolunteerModel

/**
 * @author Ananda Camara
 */
interface OnItemClickDelegate {
    fun onItemClick(item: ItemModel)
    fun onVolunteerClick(volunteer: VolunteerModel)
}