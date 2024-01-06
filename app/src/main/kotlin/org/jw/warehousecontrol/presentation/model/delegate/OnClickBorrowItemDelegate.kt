package org.jw.warehousecontrol.presentation.model.delegate

import org.jw.warehousecontrol.presentation.model.GenericListItem

/**
 * @author Ananda Camara
 */
internal interface OnClickBorrowItemDelegate {
    fun onItemClick(item: GenericListItem)
}