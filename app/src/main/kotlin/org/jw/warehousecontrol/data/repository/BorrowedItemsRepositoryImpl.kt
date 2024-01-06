package org.jw.warehousecontrol.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take
import org.jw.warehousecontrol.data.dao.BorrowedItemDao
import org.jw.warehousecontrol.data.dao.ItemDao
import org.jw.warehousecontrol.data.dao.VolunteerDao
import org.jw.warehousecontrol.data.model.*
import org.jw.warehousecontrol.data.model.ItemModel
import org.jw.warehousecontrol.data.model.VolunteerModel
import org.jw.warehousecontrol.data.model.toBorrowedItemModel
import org.jw.warehousecontrol.data.model.toEntity
import org.jw.warehousecontrol.domain.model.ItemEntity
import org.jw.warehousecontrol.domain.model.BorrowedItemsEntity
import org.jw.warehousecontrol.domain.model.VolunteerEntity
import org.jw.warehousecontrol.domain.repository.BorrowedItemsRepository

/**
 * @author Ananda Camara
 */
internal class BorrowedItemsRepositoryImpl(
    private val borrowedItemDao: BorrowedItemDao,
    private val itemDao: ItemDao,
    private val volunteerDao: VolunteerDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : BorrowedItemsRepository {

    override suspend fun getBorrowedItems(): BorrowedItemsEntity {
        val itemsDict = mutableMapOf<ItemEntity, MutableList<VolunteerEntity>>()
        val volunteersDict = mutableMapOf<VolunteerEntity, MutableList<ItemEntity>>()

        borrowedItemDao.getAllItems().flowOn(dispatcher).take(1).collect {
            it.forEach { item ->
                val itemEntity = item.toEntity()
                if (itemsDict[itemEntity] == null) itemsDict[itemEntity] = mutableListOf()

                item.volunteers.forEach { volunteer ->
                    val volunteerEntity = VolunteerEntity(name = volunteer)
                    if (volunteersDict[volunteerEntity] == null)
                        volunteersDict[volunteerEntity] = mutableListOf()

                    itemsDict[itemEntity]?.add(volunteerEntity)
                    volunteersDict[volunteerEntity]?.add(itemEntity)
                }
            }

        }

        return BorrowedItemsEntity(itemsDict, volunteersDict)
    }

    override suspend fun lendItems(vararg items: ItemEntity, to: VolunteerEntity) =
        items.forEach { item ->
            val itemModel = item.toBorrowedItemModel()
            addItemOnDatabase(item.toModel())

            borrowedItemDao.getItemById(item.id).flowOn(dispatcher).take(1).collect {
                when (val savedItem = it) {
                    null -> {
                        itemModel.volunteers = listOf(to.name)
                        borrowedItemDao.insertItem(itemModel)
                    }
                    else -> {
                        itemModel.volunteers = savedItem.volunteers + to.name
                        borrowedItemDao.updateItem(itemModel)
                    }
                }
            }
        }.also { addVolunteerOnDatabase(to.toModel()) }

    override suspend fun returnItem(item: ItemEntity, from: VolunteerEntity) {
        val itemModel = item.toBorrowedItemModel()
        borrowedItemDao.getItemById(item.id).flowOn(dispatcher).take(1).collect {
            val itemToBeDeleted = it?.volunteers?.indexOfFirst { name -> name == from.name }
            it?.volunteers?.toMutableList()?.let { volunteers ->
                itemToBeDeleted?.let { index -> volunteers.removeAt(index) }
                itemModel.volunteers = volunteers
            }

            if (itemModel.volunteers.isEmpty()) borrowedItemDao.deleteItem(itemModel)
            else borrowedItemDao.updateItem(itemModel)
        }
    }

    private suspend fun addItemOnDatabase(item: ItemModel) {
        itemDao.insertItem(item)
    }

    private suspend fun addVolunteerOnDatabase(volunteer: VolunteerModel) {
        volunteerDao.insertVolunteer(volunteer)
    }
}