package org.jw.warehousecontrol.domain.repository

import org.jw.warehousecontrol.domain.model.ItemEntity
import org.jw.warehousecontrol.domain.model.VolunteerEntity

/**
 * @author Ananda Camara
 */
internal interface SavedItemsRepository {
    suspend fun getSavedItems(): List<ItemEntity>
    suspend fun getSavedVolunteers(): List<VolunteerEntity>
    suspend fun saveItems(vararg items: ItemEntity)
    suspend fun saveVolunteers(vararg volunteers: VolunteerEntity)
    suspend fun cleanSavedItemsDatabase()
    suspend fun cleanSavedVolunteersDatabase()
}