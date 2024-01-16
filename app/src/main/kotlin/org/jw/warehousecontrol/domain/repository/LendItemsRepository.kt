package org.jw.warehousecontrol.domain.repository

import org.jw.warehousecontrol.domain.model.EntityReference
import org.jw.warehousecontrol.domain.model.VolunteerEntity

internal interface LendItemsRepository {
    suspend fun lendItems(
        vararg items: EntityReference,
        to: VolunteerEntity
    )
}