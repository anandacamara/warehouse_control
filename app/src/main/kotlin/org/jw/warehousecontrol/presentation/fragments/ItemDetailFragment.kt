package org.jw.warehousecontrol.presentation.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import org.jw.warehousecontrol.databinding.FragmentItemDetailBinding
import org.jw.warehousecontrol.presentation.adapter.BorrowItemArrayAdapter
import org.jw.warehousecontrol.presentation.adapter.BorrowedItemAdapter
import org.jw.warehousecontrol.presentation.model.UIItem
import org.jw.warehousecontrol.presentation.model.UIItemReference
import org.jw.warehousecontrol.presentation.model.enums.TabTypeEnum
import org.jw.warehousecontrol.presentation.model.state.ItemDetailState
import org.jw.warehousecontrol.presentation.util.doNothing
import org.jw.warehousecontrol.presentation.viewmodel.ItemDetailViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel

/**
 * @author Ananda Camara
 */
internal class ItemDetailFragment : BaseDetailManagementFragment() {
    private lateinit var view: FragmentItemDetailBinding

    private val viewModel: ItemDetailViewModel by activityViewModel()

    private val args by navArgs<ItemDetailFragmentArgs>()

    private val recyclerViewAdapter by lazy {
        BorrowedItemAdapter(this, args.detailItem.associatedVolunteers.toMutableList())
    }

    private var searchAdapter: BorrowItemArrayAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        view = FragmentItemDetailBinding.inflate(inflater, container, false)
        return view.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getRegisteredVolunteers()
        setupViews()
        setupRecyclerViewAdapter()
        setupObservers()
    }

    override fun onReturnItem(position: Int) {
        showConfirmationDialog(position) {
            showLoad()
            viewModel.returnItem(
                args.detailItem.item,
                recyclerViewAdapter.getItem(position).uiItem
            )
            recyclerViewAdapter.removeItem(position)
            updateAmountItemsView()
        }
    }

    override fun onItemClick(uiItem: UIItem, tabTypeEnum: TabTypeEnum) {
        val reference = UIItemReference(uiItem)
        view.searchEditText.setText(EMPTY_STRING)
        viewModel.lendItem(args.detailItem.item, reference)
        recyclerViewAdapter.addNewItem(reference)

        updateAmountItemsView()
    }

    private fun getRegisteredVolunteers() =
        viewModel.getRegisteredVolunteers()

    private fun setupViews() = with(args.detailItem) {
        view.itemName.text = item.name
        item.img?.let { Picasso.get().load(it).into(view.itemImage) }
        updateAmountItemsView()
    }

    private fun updateAmountItemsView() {
        view.itemQuantity.text =
            BORROWED_AMOUNT.format(recyclerViewAdapter.allItemsCount)
    }

    private fun setupRecyclerViewAdapter() {
        view.recyclerView.adapter = recyclerViewAdapter
    }

    private fun setupAutocompleteAdapter(volunteers: List<UIItem>) {
        searchAdapter = BorrowItemArrayAdapter(
            requireActivity(),
            this@ItemDetailFragment,
            volunteers.toMutableList(),
            TabTypeEnum.VOLUNTEER
        )
        view.searchEditText.setAdapter(
            searchAdapter
        )
    }

    private fun setupObservers() = lifecycleScope.launch {
        viewModel.stateFlow.collect {
            onStateChange(it)
        }
    }

    private fun onStateChange(state: ItemDetailState) = when (state) {
        is ItemDetailState.Success -> setupAutocompleteAdapter(state.items)
        is ItemDetailState.ShowSuccessMessage -> showSnackbar(view.root, state.message)
        ItemDetailState.None -> doNothing()
    }.also {
        hideLoad()
    }

    companion object {
        private const val BORROWED_AMOUNT = "Quantidade em uso: %s"
        private const val EMPTY_STRING = ""
    }
}