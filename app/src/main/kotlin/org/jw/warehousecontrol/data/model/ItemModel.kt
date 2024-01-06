package org.jw.warehousecontrol.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.jw.warehousecontrol.data.util.convertToBase64
import org.jw.warehousecontrol.domain.model.ItemEntity

/**
 * @author Ananda Camara
 */
@Entity(tableName = "items_table")
internal data class ItemModel(
    @PrimaryKey
    @SerializedName("id") val id: String,
    @SerializedName("itemName") val itemName: String,
    @SerializedName("itemDescription") val itemDescription: String,
    @SerializedName("itemUrlImage") val itemUrlImage: String,
)

internal fun ItemEntity.toModel() = ItemModel(
    id = id.ifEmpty { itemName.convertToBase64() },
    itemName = itemName,
    itemDescription = itemDescription,
    itemUrlImage = itemUrlImage,
)

internal fun ItemModel.toEntity() = ItemEntity(
    id = id,
    itemName = itemName,
    itemDescription = itemDescription,
    itemUrlImage = itemUrlImage,
)
