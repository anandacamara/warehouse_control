package org.jw.warehousecontrol.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.launch
import org.jw.warehousecontrol.R
import org.jw.warehousecontrol.databinding.FragmentListBinding
import org.jw.warehousecontrol.presentation.adapter.ListAdapter
import org.jw.warehousecontrol.presentation.model.*
import org.jw.warehousecontrol.presentation.model.delegate.OnItemClickDelegate
import org.jw.warehousecontrol.presentation.model.enums.TabTypeEnum
import org.jw.warehousecontrol.presentation.model.state.ListItemsState
import org.jw.warehousecontrol.presentation.viewmodel.ListViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * @author Ananda Camara
 */
internal class ListFragment : BaseFragment(), OnItemClickDelegate {
    private lateinit var view: FragmentListBinding

    private val viewModel: ListViewModel by activityViewModel()

    private val adapter = ListAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = FragmentListBinding.inflate(inflater, container, false)
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getItems()
        setupAdapter()
        setupListeners()
        setupObservables()
    }

    override fun onItemClick(uiItem: UIItem, type: TabTypeEnum) {
        when (type) {
            TabTypeEnum.ITEM -> {
                val detailItem = ItemDetailModel(uiItem, viewModel.getAssociatedVolunteers(uiItem))
                goToItemDetailFragment(detailItem)
            }
            TabTypeEnum.VOLUNTEER -> {
                val detailVolunteer =
                    VolunteerDetailModel(uiItem, viewModel.getAssociatedItems(uiItem))
                goToVolunteerDetailFragment(detailVolunteer)
            }
        }
    }

    private fun getItems() {
        showLoad()
        viewModel.getItems()
    }

    private fun setupAdapter() {
        view.recyclerView.adapter = adapter
    }

    private fun setupListeners() {
        view.tabLayout.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.position?.let { onTabSelected(it) }
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {}
                override fun onTabUnselected(tab: TabLayout.Tab?) {}
            }
        )

        view.searchEditText.doAfterTextChanged {
            performSearch(it.toString())
        }

        view.newLendButton.setOnClickListener {
            goToLendFragment()
        }
    }

    private fun onTabSelected(position: Int) = when (position) {
        0 -> {
            adapter.loadItemsList()
            changeSearchHint(R.string.warehouse_control_item_hint)
        }
        1 -> {
            adapter.loadVolunteersList()
            changeSearchHint(R.string.warehouse_control_volunteer_hint)
        }
        else -> {}
    }.also { view.searchEditText.setText(EMPTY_STRING) }

    private fun setupObservables() = lifecycleScope.launch {
        viewModel.stateFlow.collect { onStateChange(it) }
    }

    private fun onStateChange(state: ListItemsState) = when (state) {
        is ListItemsState.GetListItemsSuccess -> adapter.loadList(
            state.itemsList,
            state.volunteersList
        )
        ListItemsState.Failure -> showFailureSnackbar()
        else -> {}
    }.also { hideLoad() }

    private fun showFailureSnackbar() {
        Snackbar.make(
            view.root,
            R.string.warehouse_control_error_load_list_message,
            Snackbar.LENGTH_SHORT
        )
            .show()
    }

    private fun performSearch(searchText: String) = adapter.performSearch(searchText)

    private fun changeSearchHint(resourceString: Int) =
        view.searchInputLayout.setHint(resourceString)

    private fun goToLendFragment() {
        val action = ListFragmentDirections.goToLendFragment()
        findNavController().navigate(action)
    }

    private fun goToItemDetailFragment(detailItem: ItemDetailModel) =
        adapter.volunteersList?.items?.let {
            val action = ListFragmentDirections.goToItemDetailsFragment(detailItem)
            findNavController().navigate(action)
        }

    private fun goToVolunteerDetailFragment(detailVolunteer: VolunteerDetailModel) =
        adapter.itemsList?.items?.let {
            val action = ListFragmentDirections.goToVolunteerDetailsFragment(detailVolunteer)
            findNavController().navigate(action)
        }
    companion object {
        private const val EMPTY_STRING = ""
    }
}