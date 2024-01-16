package org.jw.warehousecontrol.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take
import org.jw.warehousecontrol.data.dao.BorrowedItemDao
import org.jw.warehousecontrol.data.model.BorrowedItemModel
import org.jw.warehousecontrol.data.model.toEntity
import org.jw.warehousecontrol.domain.model.BorrowedItemEntity
import org.jw.warehousecontrol.domain.model.EntityReference
import org.jw.warehousecontrol.domain.model.ItemEntity
import org.jw.warehousecontrol.domain.model.VolunteerEntity
import org.jw.warehousecontrol.domain.repository.BorrowedItemsRepository

/**
 * @author Ananda Camara
 */
internal class BorrowedItemsRepositoryImpl(
    private val borrowedItemDao: BorrowedItemDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BorrowedItemsRepository {

    override suspend fun getBorrowedItems(): BorrowedItemEntity {
        val itemsDict = mutableMapOf<ItemEntity, MutableList<EntityReference>>()
        val volunteersDict = mutableMapOf<VolunteerEntity, MutableList<EntityReference>>()

        borrowedItemDao.getAllItems().flowOn(dispatcher).take(1).collect {
            it.populateMaps(itemsDict, volunteersDict)
        }

        return BorrowedItemEntity(itemsDict, volunteersDict)
    }

    override suspend fun cleanBorrowedItemsDatabase() =
        borrowedItemDao.getAllItems().flowOn(dispatcher).collect {
            it.forEach { item ->
                borrowedItemDao.deleteItem(item)
            }
        }

    private fun List<BorrowedItemModel>.populateMaps(
        itemDict: MutableMap<ItemEntity, MutableList<EntityReference>>,
        volunteerDict: MutableMap<VolunteerEntity, MutableList<EntityReference>>
    ) = forEach {
        val itemEntity = it.toEntity()
        itemDict[itemEntity] = itemDict[itemEntity] ?: mutableListOf()

        it.volunteers.forEach { volunteer ->
            val volunteerEntity = VolunteerEntity(name = volunteer.name)
            volunteerDict[volunteerEntity] = volunteerDict[volunteerEntity] ?: mutableListOf()

            itemDict[itemEntity]?.add(EntityReference(volunteerEntity, volunteer.count))
            volunteerDict[volunteerEntity]?.add(EntityReference(itemEntity, volunteer.count))
        }
    }
}