package org.jw.warehousecontrol.data.model

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

internal data class BorrowedVolunteerModel(
    @PrimaryKey
    @SerializedName("name") val name: String,
    @SerializedName("quantity") var count: Int,
)