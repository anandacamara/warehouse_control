package org.jw.warehousecontrol.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @author Ananda Camara
 */
@Parcelize
data class VolunteerDetailModel(
    val volunteer: UIItem,
    val associatedItems: List<UIItemReference>
): Parcelable
