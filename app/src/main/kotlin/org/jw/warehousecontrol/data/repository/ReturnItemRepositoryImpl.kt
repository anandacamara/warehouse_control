package org.jw.warehousecontrol.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take
import org.jw.warehousecontrol.data.dao.BorrowedItemDao
import org.jw.warehousecontrol.data.model.BorrowedItemModel
import org.jw.warehousecontrol.data.model.BorrowedVolunteerModel
import org.jw.warehousecontrol.data.model.toBorrowedVolunteerModel
import org.jw.warehousecontrol.domain.model.EntityReference
import org.jw.warehousecontrol.domain.model.VolunteerEntity
import org.jw.warehousecontrol.domain.model.toBorrowedItemModel
import org.jw.warehousecontrol.domain.repository.ReturnItemRepository

internal class ReturnItemRepositoryImpl(
    private val borrowedItemDao: BorrowedItemDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ReturnItemRepository {

    override suspend fun returnItem(item: EntityReference, from: VolunteerEntity) {
        val itemModel = item.toBorrowedItemModel()
        borrowedItemDao.getItemById(itemModel.id).flowOn(dispatcher).take(1).collect {
            itemModel.volunteers =
                updateVolunteersList(it, from.toBorrowedVolunteerModel(item.count))

            if (itemModel.volunteers.isEmpty()) borrowedItemDao.deleteItem(itemModel)
            else borrowedItemDao.updateItem(itemModel)
        }
    }

    private fun updateVolunteersList(
        item: BorrowedItemModel?,
        volunteer: BorrowedVolunteerModel
    ): List<BorrowedVolunteerModel> = when (item) {
        null -> listOf()
        else -> {
            val index = item.volunteers.indexOfFirst { volunteer.name == it.name }
            if (index < 0) item.volunteers
            else if (item.volunteers[index].count > 1) item.volunteers[index].count--
            else item.volunteers = item.volunteers - item.volunteers[index]
            item.volunteers
        }
    }
}