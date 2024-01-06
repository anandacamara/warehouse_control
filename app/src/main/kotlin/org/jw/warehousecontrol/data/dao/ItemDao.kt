package org.jw.warehousecontrol.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.jw.warehousecontrol.data.model.ItemModel

/**
 * @author Ananda Camara
 */
@Dao
internal abstract class ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertItem(item: ItemModel)

    @Delete
    abstract suspend fun deleteItem(item: ItemModel)

    @Query("SELECT * FROM items_table")
    abstract fun getAllItems(): Flow<List<ItemModel>>
}