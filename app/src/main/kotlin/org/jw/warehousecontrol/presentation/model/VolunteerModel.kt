package org.jw.warehousecontrol.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.jw.warehousecontrol.R
import org.jw.warehousecontrol.domain.model.VolunteerEntity

/**
 * @author Ananda Camara
 */
@Parcelize
data class VolunteerModel(
    override val name: String,
    val builderCode: String? = null
): Parcelable, GenericListItem(name, R.drawable.warehouse_control_icon_volunteer, null)

internal fun VolunteerEntity.toModel() = VolunteerModel(
    name = name,
    builderCode = builderCode
)

internal fun VolunteerModel.toEntity() = VolunteerEntity(
    name = name,
    builderCode = builderCode
)