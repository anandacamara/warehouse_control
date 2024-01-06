package org.jw.warehousecontrol.presentation.fragments

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import org.jw.warehousecontrol.presentation.model.delegate.BaseDelegate

/**
 * @author Ananda Camara
 */
internal open class BaseFragment : Fragment() {
    private val delegate by lazy { activity as BaseDelegate }

    fun showLoad() = delegate.showLoad()

    fun hideLoad() = delegate.hideLoad()

    fun showSnackbar(view: View, message: String) {
        Snackbar.make(
            view,
            message,
            Snackbar.LENGTH_SHORT
        )
            .show()
    }
}