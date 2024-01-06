package org.jw.warehousecontrol.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author Ananda Camara
 */
@Parcelize
data class ItemDetailModel(
    val item: ItemModel,
    val associatedVolunteers: List<VolunteerModel>
): Parcelable
