package org.jw.warehousecontrol.domain.model

/**
 * @author Ananda Camara
 */
internal data class BorrowedItemsEntity(
    val itemsDict: Map<ItemEntity, List<VolunteerEntity>>,
    val volunteersDict: Map<VolunteerEntity, List<ItemEntity>>
)
