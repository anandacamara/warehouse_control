package org.jw.warehousecontrol.domain.repository

import org.jw.warehousecontrol.domain.model.ItemEntity
import org.jw.warehousecontrol.domain.model.BorrowedItemsEntity
import org.jw.warehousecontrol.domain.model.VolunteerEntity

/**
 * @author Ananda Camara
 */
internal interface BorrowedItemsRepository {
    suspend fun getBorrowedItems() : BorrowedItemsEntity

    suspend fun lendItems(
        vararg items: ItemEntity,
        to: VolunteerEntity
    )

    suspend fun returnItem(
        item: ItemEntity,
        from: VolunteerEntity
    )
}