package org.jw.warehousecontrol.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.jw.warehousecontrol.data.util.convertToBase64
import org.jw.warehousecontrol.domain.model.ItemEntity

/**
 * @author Ananda Camara
 */
@Entity(tableName = "borrowed_items_table")
internal data class BorrowedItemModel(
    @PrimaryKey
    @SerializedName("id") val id: String,
    @SerializedName("itemName") val itemName: String,
    @SerializedName("itemDescription") val itemDescription: String,
    @SerializedName("itemUrlImage") val itemUrlImage: String,
    @SerializedName("volunteers") var volunteers: List<BorrowedVolunteerModel>
)

internal fun ItemEntity.toBorrowedItemModel() = BorrowedItemModel(
    id = id.ifEmpty { itemName.lowercase().convertToBase64() },
    itemName = itemName.lowercase(),
    itemDescription = itemDescription,
    itemUrlImage = itemUrlImage,
    volunteers = listOf()
)

internal fun BorrowedItemModel.toEntity() = ItemEntity(
    id = id,
    itemName = itemName,
    itemDescription = itemDescription,
    itemUrlImage = itemUrlImage,
)