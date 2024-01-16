package org.jw.warehousecontrol.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.jw.warehousecontrol.data.util.convertToBase64
import org.jw.warehousecontrol.domain.model.VolunteerEntity

/**
 * @author Ananda Camara
 */
@Entity(tableName = "volunteers_table")
internal data class VolunteerModel(
    @PrimaryKey
    @SerializedName("id") val id: String,
    @SerializedName("volunteerName") val volunteerName: String,
)

internal fun VolunteerEntity.toModel() = VolunteerModel(
    id = id.ifEmpty { name.convertToBase64() },
    volunteerName = name
)

internal fun VolunteerModel.toEntity() = VolunteerEntity(
    id = id,
    name = volunteerName
)

internal fun VolunteerEntity.toBorrowedVolunteerModel(count: Int) = BorrowedVolunteerModel(
    name = name,
    count = count
)