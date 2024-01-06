package org.jw.warehousecontrol.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.jw.warehousecontrol.domain.model.BorrowedItemsEntity

/**
 * @author Ananda Camara
 */
@Parcelize
data class ListItemsModel(
    val itemsDict: Map<ItemModel, List<VolunteerModel>>,
    val volunteersDict: Map<VolunteerModel, List<ItemModel>>
) : Parcelable

internal fun BorrowedItemsEntity.toModel() =
    ListItemsModel(
        itemsDict = itemsDict.entries.associate {
            it.key.toModel() to it.value.map { v -> v.toModel() }
        },
        volunteersDict = volunteersDict.entries.associate {
            it.key.toModel() to it.value.map { i -> i.toModel() }
        }
    )