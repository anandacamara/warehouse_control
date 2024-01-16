package org.jw.warehousecontrol.domain.repository

import org.jw.warehousecontrol.domain.model.BorrowedItemEntity
import org.jw.warehousecontrol.domain.model.EntityReference
import org.jw.warehousecontrol.domain.model.VolunteerEntity

/**
 * @author Ananda Camara
 */
internal interface BorrowedItemsRepository {
    suspend fun getBorrowedItems() : BorrowedItemEntity

    suspend fun cleanBorrowedItemsDatabase()
}