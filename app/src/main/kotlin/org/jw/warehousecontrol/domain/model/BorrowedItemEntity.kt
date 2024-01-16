package org.jw.warehousecontrol.domain.model

/**
 * @author Ananda Camara
 */
internal data class BorrowedItemEntity(
    val itemsDict: Map<ItemEntity, List<EntityReference>>,
    val volunteersDict: Map<VolunteerEntity, List<EntityReference>>
)
