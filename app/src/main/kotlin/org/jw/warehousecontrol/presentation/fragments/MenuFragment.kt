package org.jw.warehousecontrol.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.jw.warehousecontrol.R
import org.jw.warehousecontrol.databinding.FragmentMenuBinding
import org.jw.warehousecontrol.presentation.model.state.MenuState
import org.jw.warehousecontrol.presentation.util.doNothing
import org.jw.warehousecontrol.presentation.util.isNetworkAvailable
import org.jw.warehousecontrol.presentation.viewmodel.MenuViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * @author Ananda Camara
 */
internal class MenuFragment : BaseFragment() {
    private lateinit var view: FragmentMenuBinding

    private val viewModel: MenuViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = FragmentMenuBinding.inflate(inflater, container, false)
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        view.addNewItemButton.setOnClickListener { goToItemRegisterFragment() }
        view.managerButton.setOnClickListener { goToListFragment() }
        view.syncButton.setOnClickListener {
            verifyNetworkAvailability { showSyncAlertDialog() }
        }
    }

    private fun setupObservers() = lifecycleScope.launch {
        viewModel.stateFlow.collect {
            onStateChange(it)
        }
    }

    private fun onStateChange(state: MenuState) = when (state) {
        MenuState.Failure -> showSnackbar(
            view.root,
            getString(R.string.warehouse_control_confirmation_sync_error_message)
        ).also { hideLoad() }
        MenuState.None -> doNothing()
        MenuState.Success -> showSnackbar(
            view.root,
            getString(R.string.warehouse_control_confirmation_sync_success_message)
        ).also { hideLoad() }
    }

    private fun showSyncAlertDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.warehouse_control_confirmation_sync_title)
            .setMessage(R.string.warehouse_control_confirmation_sync_message)
            .setNegativeButton(resources.getString(R.string.warehouse_control_no_message)) { _, _ ->
                doNothing()
            }
            .setPositiveButton(resources.getString(R.string.warehouse_control_yes_message)) { _, _ ->
                showLoad()
                viewModel.refreshItems()
            }
            .show()

    }

    private fun verifyNetworkAvailability(doSomething: () -> Unit) {
        if (isNetworkAvailable(requireContext())) doSomething()
        else showSnackbar(
            view.root,
            getString(R.string.warehouse_needed_network_availability_message)
        )
    }

    private fun goToListFragment() {
        val action = MenuFragmentDirections.goToListFragment()
        findNavController().navigate(action)
    }

    private fun goToItemRegisterFragment() {
        val action = MenuFragmentDirections.goToItemRegisterFragment()
        findNavController().navigate(action)
    }
}