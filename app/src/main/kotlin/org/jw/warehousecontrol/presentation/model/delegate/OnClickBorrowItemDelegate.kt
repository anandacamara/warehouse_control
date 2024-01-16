package org.jw.warehousecontrol.presentation.model.delegate

import org.jw.warehousecontrol.presentation.model.UIItem
import org.jw.warehousecontrol.presentation.model.enums.TabTypeEnum

/**
 * @author Ananda Camara
 */
internal interface OnClickBorrowItemDelegate {
    fun onItemClick(uiItem: UIItem, tabTypeEnum: TabTypeEnum)
}