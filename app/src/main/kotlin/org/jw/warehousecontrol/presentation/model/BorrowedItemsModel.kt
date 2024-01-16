package org.jw.warehousecontrol.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.jw.warehousecontrol.domain.model.BorrowedItemEntity
import org.jw.warehousecontrol.domain.model.ItemEntity
import org.jw.warehousecontrol.domain.model.VolunteerEntity

/**
 * @author Ananda Camara
 */
@Parcelize
data class BorrowedItemsModel(
    val itemsDict: Map<UIItem, List<UIItemReference>>,
    val volunteersDict: Map<UIItem, List<UIItemReference>>
) : Parcelable

internal fun BorrowedItemEntity.toModel() =
    BorrowedItemsModel(
        itemsDict = itemsDict.entries.associate {
            it.key.toUIItem() to it.value.map { v -> v.toUIItemReference() }
        },
        volunteersDict = volunteersDict.entries.associate {
            it.key.toUIItem() to it.value.map { i -> i.toUIItemReference() }
        }
    )