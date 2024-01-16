package org.jw.warehousecontrol.domain.model

import org.jw.warehousecontrol.data.model.toBorrowedItemModel
import org.jw.warehousecontrol.data.model.toModel

internal data class EntityReference(
    val entity: GenericEntity,
    val count: Int = 1
)

internal open class GenericEntity

internal fun EntityReference.toBorrowedItemModel() =
    (this.entity as ItemEntity).toBorrowedItemModel()

internal fun EntityReference.toModel() = (this.entity as ItemEntity).toModel()