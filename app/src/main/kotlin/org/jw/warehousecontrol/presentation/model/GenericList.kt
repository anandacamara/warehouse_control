package org.jw.warehousecontrol.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.jw.warehousecontrol.presentation.model.enums.TabTypeEnum

/**
 * @author Ananda Camara
 */
@Parcelize
internal data class GenericList(
    val tabType: TabTypeEnum,
    val items: List<GenericListItem>
): Parcelable