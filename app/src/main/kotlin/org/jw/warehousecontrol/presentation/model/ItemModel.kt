package org.jw.warehousecontrol.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.jw.warehousecontrol.R
import org.jw.warehousecontrol.domain.model.EntityReference
import org.jw.warehousecontrol.domain.model.ItemEntity

/**
 * @author Ananda Camara
 */
@Parcelize
data class ItemModel(
    override val name: String,
    override val img: String? = null,
    override var count: Int = 1,
    val description: String? = null,
    val id: String? = null
): Parcelable, GenericListItem(name, R.drawable.warehouse_control_icon_tools, img, count)

internal fun ItemEntity.toModel() = ItemModel(
    name = itemName.replaceFirstChar { it.uppercase() },
    img = itemUrlImage.ifEmpty { null },
    description = itemDescription,
    id = id
)

internal fun ItemModel.toEntity() = ItemEntity(
    id = id.orEmpty(),
    itemName = name,
    itemUrlImage = img.orEmpty(),
    itemDescription = description.orEmpty()
)

internal fun ItemModel.toEntityReference() = EntityReference(
    entity = toEntity(),
    count = count
)