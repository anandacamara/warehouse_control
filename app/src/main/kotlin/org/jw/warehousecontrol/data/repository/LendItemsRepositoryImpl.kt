package org.jw.warehousecontrol.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take
import org.jw.warehousecontrol.data.dao.BorrowedItemDao
import org.jw.warehousecontrol.data.dao.ItemDao
import org.jw.warehousecontrol.data.dao.VolunteerDao
import org.jw.warehousecontrol.data.model.*
import org.jw.warehousecontrol.domain.model.EntityReference
import org.jw.warehousecontrol.domain.model.VolunteerEntity
import org.jw.warehousecontrol.domain.model.toBorrowedItemModel
import org.jw.warehousecontrol.domain.model.toModel
import org.jw.warehousecontrol.domain.repository.LendItemsRepository

internal class LendItemsRepositoryImpl(
    private val borrowedItemDao: BorrowedItemDao,
    private val itemDao: ItemDao,
    private val volunteerDao: VolunteerDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): LendItemsRepository {

    override suspend fun lendItems(vararg items: EntityReference, to: VolunteerEntity) =
        items.forEach { item ->
            val itemModel = item.toBorrowedItemModel()
            addItemOnDatabase(item.toModel())

            borrowedItemDao.getItemById(itemModel.id).flowOn(dispatcher).take(1).collect {
                itemModel.volunteers =
                    updateVolunteersList(it, to.toBorrowedVolunteerModel(item.count))

                borrowedItemDao.insertItem(itemModel)
            }
        }.also { addVolunteerOnDatabase(to.toModel()) }

    private suspend fun addItemOnDatabase(item: ItemModel) = itemDao.insertItem(item)

    private suspend fun addVolunteerOnDatabase(volunteer: VolunteerModel) =
        volunteerDao.insertVolunteer(volunteer)

    private fun updateVolunteersList(
        item: BorrowedItemModel?,
        volunteer: BorrowedVolunteerModel
    ): List<BorrowedVolunteerModel> =
        when (item) {
            null -> listOf(volunteer)
            else -> {
                val index = item.volunteers.indexOfFirst { it.name == volunteer.name }
                if (index < 0) item.volunteers + volunteer
                else {
                    item.volunteers[index].count += volunteer.count
                    item.volunteers
                }
            }
        }
}