package org.jw.warehousecontrol.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import kotlinx.coroutines.launch
import org.jw.warehousecontrol.R
import org.jw.warehousecontrol.databinding.FragmentVolunteerDetailsBinding
import org.jw.warehousecontrol.presentation.adapter.BorrowItemArrayAdapter
import org.jw.warehousecontrol.presentation.adapter.BorrowedItemAdapter
import org.jw.warehousecontrol.presentation.model.GenericListItem
import org.jw.warehousecontrol.presentation.model.ItemModel
import org.jw.warehousecontrol.presentation.model.enums.TabTypeEnum
import org.jw.warehousecontrol.presentation.model.state.VolunteerDetailState
import org.jw.warehousecontrol.presentation.util.doNothing
import org.jw.warehousecontrol.presentation.viewmodel.VolunteerDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * @author Ananda Camara
 */
internal class VolunteerDetailsFragment : BaseDetailManagementFragment() {
    private lateinit var view: FragmentVolunteerDetailsBinding

    private val viewModel: VolunteerDetailsViewModel by activityViewModel()

    private val args by navArgs<VolunteerDetailsFragmentArgs>()

    private val recyclerViewAdapter by lazy {
        BorrowedItemAdapter(this, args.detailVolunteer.associatedItems.toMutableList())
    }

    private var searchAdapter: BorrowItemArrayAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = FragmentVolunteerDetailsBinding.inflate(inflater, container, false)
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getRegisteredItems()
        setupViews()
        setupRecyclerViewAdapter()
        setupObservers()
    }

    override fun onReturnItem(position: Int) {
        showConfirmationDialog(position) {
            showLoad()
            viewModel.returnItem(
                recyclerViewAdapter.getItem(position) as ItemModel,
                args.detailVolunteer.volunteer
            )
            recyclerViewAdapter.removeItem(position)
        }
    }

    override fun onItemClick(item: GenericListItem) = with(item as ItemModel) {
        view.searchEditText.setText(EMPTY_STRING)
        viewModel.lendItem(item, args.detailVolunteer.volunteer)

        recyclerViewAdapter.addNewItem(item)
    }

    private fun getRegisteredItems() {
        showLoad()
        viewModel.getRegisteredItems()
    }

    private fun setupViews() = with(args.detailVolunteer) {
        view.volunteerName.text = volunteer.name
        volunteer.builderCode?.let {
            view.volunteerBuilder.text = BUILDER_NUMBER.format(it)
            view.volunteerBuilder.visibility = View.VISIBLE
        }
    }

    private fun setupRecyclerViewAdapter() {
        view.recyclerView.adapter = recyclerViewAdapter
    }

    private fun setupObservers() = lifecycleScope.launch {
        viewModel.stateFlow.collect {
            onStateChange(it)
        }
    }

    private fun onStateChange(state: VolunteerDetailState) = when (state) {
        is VolunteerDetailState.Success -> setupAutocompleteAdapter(state.items)
        VolunteerDetailState.Failure -> showSnackbar(
            view.root,
            getString(R.string.warehouse_control_error_load_list_message)
        )
        VolunteerDetailState.None -> doNothing()
        is VolunteerDetailState.ShowSuccessMessage -> showSnackbar(
            view.root,
            state.message
        )
    }.also { hideLoad() }

    private fun setupAutocompleteAdapter(items: List<ItemModel>) {
        searchAdapter = BorrowItemArrayAdapter(
            requireActivity(),
            this@VolunteerDetailsFragment,
            items.toMutableList(),
            TabTypeEnum.ITEM
        )

        view.searchEditText.setAdapter(
            searchAdapter
        )
    }

    companion object {
        private const val BUILDER_NUMBER = "Builder: %s"
        private const val EMPTY_STRING = ""
    }
}