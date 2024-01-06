package org.jw.warehousecontrol.presentation.model

import android.os.Parcelable
import androidx.annotation.LayoutRes
import kotlinx.parcelize.Parcelize

/**
 * @author Ananda Camara
 */
@Parcelize
open class GenericListItem(
    open val name: String,
    open val defaultResourceId: Int,
    open val img: String?
    ): Parcelable