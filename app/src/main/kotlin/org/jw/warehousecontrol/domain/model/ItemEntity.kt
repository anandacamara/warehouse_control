package org.jw.warehousecontrol.domain.model

/**
 * @author Ananda Camara
 */
internal data class ItemEntity(
    val id: String = String(),
    val itemName: String = String(),
    val itemDescription: String = String(),
    val itemUrlImage: String = String()
) : GenericEntity()
