package org.jw.warehousecontrol.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.jw.warehousecontrol.data.dao.BorrowedItemDao
import org.jw.warehousecontrol.data.dao.ItemDao
import org.jw.warehousecontrol.data.dao.VolunteerDao
import org.jw.warehousecontrol.data.model.BorrowedItemModel
import org.jw.warehousecontrol.data.model.ItemModel
import org.jw.warehousecontrol.data.model.VolunteerModel
import org.jw.warehousecontrol.data.util.ListConverter

/**
 * @author Ananda Camara
 */
@Database(
    entities = [BorrowedItemModel::class, ItemModel::class, VolunteerModel::class],
    version = 1
)
@TypeConverters(ListConverter::class)
internal abstract class WarehouseControlDatabase : RoomDatabase() {
    abstract fun borrowedItemDao(): BorrowedItemDao

    abstract fun itemDao(): ItemDao

    abstract fun volunteerDao(): VolunteerDao

    companion object {
        const val DATABASE_NAME = "warehouse_control_database"
    }
}