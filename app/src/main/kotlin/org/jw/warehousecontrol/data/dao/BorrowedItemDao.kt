package org.jw.warehousecontrol.data.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.jw.warehousecontrol.data.model.BorrowedItemModel

/**
 * @author Ananda Camara
 */
@Dao
internal abstract class BorrowedItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertItem(item: BorrowedItemModel)

    @Update
    abstract suspend fun updateItem(item: BorrowedItemModel)

    @Delete
    abstract suspend fun deleteItem(item: BorrowedItemModel)

    @Query("SELECT * FROM borrowed_items_table WHERE id = :id")
    abstract fun getItemById(id: String): Flow<BorrowedItemModel?>

    @Query("SELECT * FROM borrowed_items_table")
    abstract fun getAllItems(): Flow<List<BorrowedItemModel>>
}