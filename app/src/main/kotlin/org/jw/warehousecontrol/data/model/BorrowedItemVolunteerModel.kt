package org.jw.warehousecontrol.data.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

internal data class BorrowedItemVolunteerModel(
    @PrimaryKey
    @SerializedName("name") val name: String,
    @SerializedName("quantity") val quantity: Int,
)
