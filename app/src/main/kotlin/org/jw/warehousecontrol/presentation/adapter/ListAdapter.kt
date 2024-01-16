package org.jw.warehousecontrol.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.jw.warehousecontrol.R
import org.jw.warehousecontrol.databinding.WarehouseControlListItemBinding
import org.jw.warehousecontrol.presentation.model.UIItem
import org.jw.warehousecontrol.presentation.model.UIList
import org.jw.warehousecontrol.presentation.model.delegate.OnItemClickDelegate
import org.jw.warehousecontrol.presentation.model.enums.TabTypeEnum
import org.jw.warehousecontrol.presentation.util.unaccent

/**
 * @author Ananda Camara
 */
internal class ListAdapter(
    private val delegate: OnItemClickDelegate
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var uiList: UIList = UIList(TabTypeEnum.ITEM, listOf())

    var itemsList: UIList? = null
    var volunteersList: UIList? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.warehouse_control_list_item, parent, false)

        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = uiList.items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        with(holder as ItemViewHolder) {
            val item = uiList.items[position]

            holder.binding.icon.setImageResource(item.defaultResourceId)
            holder.binding.itemName.text = item.name
            holder.binding.root.setOnClickListener { delegate.onItemClick(item, uiList.tabType) }
        }

    fun loadList(itemsList: List<UIItem>, volunteersList: List<UIItem>) {
        this.itemsList = UIList(TabTypeEnum.ITEM, itemsList.sortedBy { it.name })
        this.volunteersList =
            UIList(TabTypeEnum.VOLUNTEER, volunteersList.sortedBy { it.name })

        loadItemsList()
    }

    fun loadItemsList() = loadItems(this.itemsList)

    fun loadVolunteersList() = loadItems(this.volunteersList)

    fun performSearch(searchText: String) {
        val currentList = when (uiList.tabType) {
            TabTypeEnum.VOLUNTEER -> volunteersList
            TabTypeEnum.ITEM -> itemsList
        }

        currentList?.items?.let { list ->
            val searchedItems =
                list.filter {
                    it.name.unaccent().contains(searchText.unaccent(), true)
                }

            loadItems(UIList(uiList.tabType, searchedItems))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadItems(list: UIList?) {
        list?.let { this.uiList = it }
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = WarehouseControlListItemBinding.bind(view)
    }
}