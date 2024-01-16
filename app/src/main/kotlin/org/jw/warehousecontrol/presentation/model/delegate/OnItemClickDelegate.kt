package org.jw.warehousecontrol.presentation.model.delegate

import org.jw.warehousecontrol.presentation.model.UIItem
import org.jw.warehousecontrol.presentation.model.enums.TabTypeEnum

/**
 * @author Ananda Camara
 */
interface OnItemClickDelegate {
    fun onItemClick(uiItem: UIItem, type: TabTypeEnum)
}