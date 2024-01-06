package org.jw.warehousecontrol.presentation.fragments

import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.jw.warehousecontrol.R
import org.jw.warehousecontrol.presentation.model.delegate.OnClickBorrowItemDelegate
import org.jw.warehousecontrol.presentation.model.delegate.OnReturnItemDelegate
import org.jw.warehousecontrol.presentation.util.doNothing

/**
 * @author Ananda Camara
 */
internal abstract class BaseDetailManagementFragment: OnReturnItemDelegate,
    OnClickBorrowItemDelegate, BaseFragment() {

    fun showConfirmationDialog(position: Int, returnItem: (Int) -> Unit) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(resources.getString(R.string.warehouse_control_confirmation_return_message))
            .setNegativeButton(resources.getString(R.string.warehouse_control_no_message)) { _, _ ->
                doNothing()
            }
            .setPositiveButton(resources.getString(R.string.warehouse_control_yes_message)) { _, _ ->
                returnItem(position)
            }
            .show()
    }
}