package org.jw.warehousecontrol.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import org.jw.warehousecontrol.R
import org.jw.warehousecontrol.databinding.WarehouseControlAddItemBinding
import org.jw.warehousecontrol.databinding.WarehouseControlListItemBinding
import org.jw.warehousecontrol.presentation.model.GenericListItem
import org.jw.warehousecontrol.presentation.model.ItemModel
import org.jw.warehousecontrol.presentation.model.VolunteerModel
import org.jw.warehousecontrol.presentation.model.delegate.OnClickBorrowItemDelegate
import org.jw.warehousecontrol.presentation.model.enums.TabTypeEnum

/**
 * @author Ananda Camara
 */
internal class BorrowItemArrayAdapter(
    context: Context,
    val delegate: OnClickBorrowItemDelegate,
    private val items: MutableList<GenericListItem>,
    private val tabTypeEnum: TabTypeEnum
) : ArrayAdapter<GenericListItem>(context, R.layout.warehouse_control_list_item, items) {
    private var searchedItems: List<GenericListItem> = items
    private var searchedValue: String = EMPTY_STRING

    override fun getFilter(): Filter = filter

    private val filter = object : Filter() {
        override fun performFiltering(chars: CharSequence?): FilterResults {
            val results = FilterResults()

            searchedItems = if (chars == null || chars.isBlank()) items
            else items.filter { it.name.contains(chars.toString(), true) }

            results.values = searchedItems
            results.count = searchedItems.size

            return results
        }

        override fun publishResults(chars: CharSequence?, results: FilterResults?) {
            chars?.let { searchedValue = it.toString() }
            notifyDataSetChanged()
        }
    }

    override fun getCount(): Int = when {
        searchedItems.isNotEmpty() -> searchedItems.size
        searchedValue.isNotEmpty() -> 1
        else -> 0
    }

    override fun getItem(position: Int): GenericListItem? = searchedItems.getOrNull(position)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View =
        createViewFromResource(position, parent)

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View =
        createViewFromResource(position, parent)

    private fun createViewFromResource(
        position: Int,
        parent: ViewGroup?
    ): View {
        if (searchedItems.isEmpty()) {
            val view = LayoutInflater.from(context)
                .inflate(R.layout.warehouse_control_add_item, parent, false)

            val binding = WarehouseControlAddItemBinding.bind(view)
            binding.name.text = REGISTER_ITEM.format(searchedValue)
            binding.root.setOnClickListener {
                val listItem = when(tabTypeEnum) {
                    TabTypeEnum.VOLUNTEER -> VolunteerModel(name=  searchedValue)
                    TabTypeEnum.ITEM -> ItemModel(name = searchedValue)
                }

                addItem(listItem)
                delegate.onItemClick(listItem)
            }

            return binding.root
        }

        val view = LayoutInflater.from(context)
            .inflate(R.layout.warehouse_control_list_item, parent, false)

        val item = searchedItems[position]
        val binding = WarehouseControlListItemBinding.bind(view)
        binding.itemName.text = item.name
        binding.root.setOnClickListener { delegate.onItemClick(item) }
        binding.icon.setImageResource(item.defaultResourceId)

        return binding.root
    }

    private fun addItem(item: GenericListItem) {
        items.add(item)
        notifyDataSetChanged()
    }

    companion object {
        private const val REGISTER_ITEM = "Cadastrar \"%s\" na lista"
        private const val EMPTY_STRING = ""
    }
}