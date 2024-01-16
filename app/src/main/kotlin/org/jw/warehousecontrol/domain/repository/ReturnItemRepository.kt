package org.jw.warehousecontrol.domain.repository

import org.jw.warehousecontrol.domain.model.EntityReference
import org.jw.warehousecontrol.domain.model.VolunteerEntity

internal interface ReturnItemRepository {
    suspend fun returnItem(
        item: EntityReference,
        from: VolunteerEntity
    )
}