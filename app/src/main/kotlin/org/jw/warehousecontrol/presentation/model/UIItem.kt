package org.jw.warehousecontrol.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.jw.warehousecontrol.R
import org.jw.warehousecontrol.domain.model.EntityReference
import org.jw.warehousecontrol.domain.model.ItemEntity
import org.jw.warehousecontrol.domain.model.VolunteerEntity

/**
 * @author Ananda Camara
 */
@Parcelize
data class UIItemReference(
    val uiItem: UIItem,
    var count: Int = 1
): Parcelable

@Parcelize
data class UIItem(
    val name: String,
    val defaultResourceId: Int,
    val img: String?
): Parcelable

internal fun VolunteerEntity.toUIItem() = UIItem(
    name = name,
    defaultResourceId = R.drawable.warehouse_control_icon_volunteer,
    img = null
)

internal fun ItemEntity.toUIItem() = UIItem(
    name = itemName.replaceFirstChar { it.uppercase() },
    defaultResourceId = R.drawable.warehouse_control_icon_tools,
    img = itemUrlImage.ifEmpty { null }
)

internal fun EntityReference.toUIItemReference() = UIItemReference(
    uiItem = when (entity) {
        is VolunteerEntity -> entity.toUIItem()
        else -> (entity as ItemEntity).toUIItem()
    },
    count = count
)

internal fun UIItem.toVolunteerEntity() = VolunteerEntity(
    name = name
)

internal fun UIItem.toItemEntity() = ItemEntity(
    itemName = name,
    itemUrlImage = img.orEmpty()
)
