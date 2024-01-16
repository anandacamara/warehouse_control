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
import org.jw.warehousecontrol.presentation.model.UIItem
import org.jw.warehousecontrol.presentation.model.delegate.OnClickBorrowItemDelegate
import org.jw.warehousecontrol.presentation.model.enums.TabTypeEnum
import org.jw.warehousecontrol.presentation.util.unaccent

/**
 * @author Ananda Camara
 */
internal class BorrowItemArrayAdapter(
    context: Context,
    val delegate: OnClickBorrowItemDelegate,
    private val items: MutableList<UIItem>,
    private val tabTypeEnum: TabTypeEnum
) : ArrayAdapter<UIItem>(context, R.layout.warehouse_control_list_item, items) {

    private var searchedItems: List<UIItem> = items
    private var searchedValue: String = EMPTY_STRING

    override fun getFilter(): Filter = filter

    private val filter = object : Filter() {
        override fun performFiltering(chars: CharSequence?): FilterResults {
            val results = FilterResults()

            searchedItems = if (chars == null || chars.isBlank()) items
            else items.filter {
                it.name.unaccent().contains(chars.toString().unaccent(), true)
            }

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

    override fun getItem(position: Int): UIItem? = searchedItems.getOrNull(position)

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
                val resourceId = when (tabTypeEnum) {
                    TabTypeEnum.VOLUNTEER -> R.drawable.warehouse_control_icon_volunteer
                    TabTypeEnum.ITEM -> R.drawable.warehouse_control_icon_tools
                }

                val uiItem = UIItem(name = searchedValue, resourceId, null)
                addItem(uiItem)
                delegate.onItemClick(uiItem, tabTypeEnum)
            }

            return binding.root
        }

        val view = LayoutInflater.from(context)
            .inflate(R.layout.warehouse_control_list_item, parent, false)

        val item = searchedItems.getOrNull(position)
        val binding = WarehouseControlListItemBinding.bind(view)

        item?.let {
            binding.itemName.text = it.name
            binding.root.setOnClickListener { delegate.onItemClick(item, tabTypeEnum) }
            binding.icon.setImageResource(it.defaultResourceId)
        }

        return binding.root
    }

    private fun addItem(item: UIItem) {
        items.add(item)
        notifyDataSetChanged()
    }

    companion object {
        private const val REGISTER_ITEM = "Cadastrar \"%s\" na lista"
        private const val EMPTY_STRING = ""
    }
}