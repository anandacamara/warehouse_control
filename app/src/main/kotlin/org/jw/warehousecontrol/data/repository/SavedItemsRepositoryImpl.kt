package org.jw.warehousecontrol.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.take
import org.jw.warehousecontrol.data.dao.ItemDao
import org.jw.warehousecontrol.data.dao.VolunteerDao
import org.jw.warehousecontrol.data.model.toEntity
import org.jw.warehousecontrol.data.model.toModel
import org.jw.warehousecontrol.domain.model.ItemEntity
import org.jw.warehousecontrol.domain.model.VolunteerEntity
import org.jw.warehousecontrol.domain.repository.SavedItemsRepository

/**
 * @author Ananda Camara
 */
internal class SavedItemsRepositoryImpl(
    private val itemDao: ItemDao,
    private val volunteerDao: VolunteerDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : SavedItemsRepository {
    override suspend fun getSavedItems(): List<ItemEntity> {
        var result = listOf<ItemEntity>()
        itemDao.getAllItems().flowOn(dispatcher).take(1).collect {
            result = it.map { item -> item.toEntity() }
        }

        return result
    }

    override suspend fun getSavedVolunteers(): List<VolunteerEntity> {
        var result = listOf<VolunteerEntity>()
        volunteerDao.getAllVolunteers().flowOn(dispatcher).take(1).collect {
            result = it.map { volunteer -> volunteer.toEntity() }
        }

        return result
    }

    override suspend fun saveItems(vararg items: ItemEntity) {
        items.forEach {
            itemDao.insertItem(it.toModel())
        }
    }

    override suspend fun saveVolunteers(vararg volunteers: VolunteerEntity) {
        volunteers.forEach {
            volunteerDao.insertVolunteer(it.toModel())
        }
    }

    override suspend fun cleanSavedItemsDatabase() {
        itemDao.getAllItems().flowOn(dispatcher).take(1).collect {
            it.forEach { item -> itemDao.deleteItem(item) }
        }
    }

    override suspend fun cleanSavedVolunteersDatabase() {
        volunteerDao.getAllVolunteers().flowOn(dispatcher).take(1).collect {
            it.forEach { volunteer -> volunteerDao.deleteVolunteer(volunteer) }
        }
    }
}