package org.jw.warehousecontrol.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import org.jw.warehousecontrol.databinding.FragmentLendBinding
import org.jw.warehousecontrol.presentation.adapter.BorrowItemArrayAdapter
import org.jw.warehousecontrol.presentation.adapter.LendItemAdapter
import org.jw.warehousecontrol.presentation.model.GenericListItem
import org.jw.warehousecontrol.presentation.model.ItemModel
import org.jw.warehousecontrol.presentation.model.VolunteerModel
import org.jw.warehousecontrol.presentation.model.delegate.EmptyListNotificationDelegate
import org.jw.warehousecontrol.presentation.model.delegate.OnClickBorrowItemDelegate
import org.jw.warehousecontrol.presentation.model.enums.TabTypeEnum
import org.jw.warehousecontrol.presentation.model.state.LendItemState
import org.jw.warehousecontrol.presentation.util.doNothing
import org.jw.warehousecontrol.presentation.viewmodel.LendViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * @author Ananda Camara
 */
internal class LendFragment : BaseFragment(), EmptyListNotificationDelegate,
    OnClickBorrowItemDelegate {
    private lateinit var view: FragmentLendBinding

    private val viewModel: LendViewModel by activityViewModel()

    private val recyclerViewAdapter = LendItemAdapter(this)

    private var listVolunteersAdapter: BorrowItemArrayAdapter? = null

    private var listItemsAdapter: BorrowItemArrayAdapter? = null

    private var selectedVolunteer: VolunteerModel? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = FragmentLendBinding.inflate(inflater, container, false)
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getVolunteersList()
        setupAdapter()
        setObservers()
        setupListeners()
    }

    override fun onItemClick(item: GenericListItem) {
        when (item) {
            is ItemModel -> recyclerViewAdapter.addItem(item)
            is VolunteerModel -> setVolunteer(item)
            else -> doNothing()
        }

        verifyEnableSaveButton()
        view.itemsEditText.dismissDropDown()
        view.volunteerEditText.dismissDropDown()
        view.itemsEditText.setText(String())
    }

    override fun notifyEmptyList() = verifyEnableSaveButton()

    private fun getVolunteersList() = viewModel.getSavedVolunteers()

    private fun setupListeners() {
        view.saveButton.setOnClickListener {
            showLoad()
            selectedVolunteer?.let {
                viewModel.registerItems(it, recyclerViewAdapter.items)
            }
        }
    }

    private fun setupAdapter() {
        view.recyclerView.adapter = recyclerViewAdapter
    }

    private fun setObservers() = lifecycleScope.launch {
        viewModel.stateFlow.collect { onStateChange(it) }
    }

    private fun onStateChange(state: LendItemState) = when (state) {
        is LendItemState.VolunteerItemSuccess -> {
            viewModel.getSavedItemsList()
            setupVolunteerAdapter(state.volunteers)
        }
        is LendItemState.ItemListSuccess -> {
            setupItemsAdapter(state.items)
            hideLoad()
        }
        LendItemState.None -> doNothing()
        LendItemState.RegisterItemsSuccess -> {
            findNavController().navigateUp()
            hideLoad()
        }
    }

    private fun areInputsFilled(): Boolean {
        if (selectedVolunteer == null || view.volunteerEditText.text.isBlank()) return false
        if (recyclerViewAdapter.items.isEmpty()) return false

        return true
    }

    private fun setupVolunteerAdapter(volunteers: List<VolunteerModel>) {
        listVolunteersAdapter =
            BorrowItemArrayAdapter(
                requireContext(),
                this,
                volunteers.toMutableList(),
                TabTypeEnum.VOLUNTEER
            )

        view.volunteerEditText.setAdapter(listVolunteersAdapter)
    }

    private fun setupItemsAdapter(items: List<ItemModel>) {
        listItemsAdapter =
            BorrowItemArrayAdapter(
                requireContext(),
                this,
                items.toMutableList(),
                TabTypeEnum.ITEM
            )

        view.itemsEditText.setAdapter(listItemsAdapter)
    }

    private fun verifyEnableSaveButton() {
        view.saveButton.isEnabled = areInputsFilled()
    }

    private fun setVolunteer(volunteerModel: VolunteerModel) {
        selectedVolunteer = volunteerModel
        view.volunteerEditText.setText(volunteerModel.name)
    }
}