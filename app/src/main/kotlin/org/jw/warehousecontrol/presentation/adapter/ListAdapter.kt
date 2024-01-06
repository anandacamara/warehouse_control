package org.jw.warehousecontrol.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.jw.warehousecontrol.R
import org.jw.warehousecontrol.databinding.WarehouseControlListItemBinding
import org.jw.warehousecontrol.presentation.model.GenericList
import org.jw.warehousecontrol.presentation.model.ItemModel
import org.jw.warehousecontrol.presentation.model.VolunteerModel
import org.jw.warehousecontrol.presentation.model.delegate.OnItemClickDelegate
import org.jw.warehousecontrol.presentation.model.enums.TabTypeEnum
import org.jw.warehousecontrol.presentation.util.unaccent

/**
 * @author Ananda Camara
 */
internal class ListAdapter(
    private val delegate: OnItemClickDelegate
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var items: GenericList = GenericList(TabTypeEnum.ITEM, listOf())

    var itemsList: GenericList? = null
    var volunteersList: GenericList? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.warehouse_control_list_item, parent, false)

        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = items.items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        with(holder as ItemViewHolder) {
            when (items.tabType) {
                TabTypeEnum.ITEM -> {
                    val item = items.items[position] as ItemModel
                    holder.binding.icon.setImageResource(item.defaultResourceId)
                    holder.binding.itemName.text = item.name
                    holder.binding.root.setOnClickListener { delegate.onItemClick(item) }
                }
                TabTypeEnum.VOLUNTEER -> {
                    val item = items.items[position] as VolunteerModel
                    holder.binding.icon.setImageResource(item.defaultResourceId)
                    holder.binding.itemName.text = item.name
                    holder.binding.root.setOnClickListener { delegate.onVolunteerClick(item) }
                }
            }
        }

    fun loadList(itemsList: List<ItemModel>, volunteersList: List<VolunteerModel>) {
        this.itemsList = GenericList(TabTypeEnum.ITEM, itemsList.sortedBy { it.name })
        this.volunteersList =
            GenericList(TabTypeEnum.VOLUNTEER, volunteersList.sortedBy { it.name })

        loadItemsList()
    }

    fun loadItemsList() = loadItems(this.itemsList)

    fun loadVolunteersList() = loadItems(this.volunteersList)

    fun searchItems(searchText: String) = itemsList?.let { list ->
        val searchedItems =
            list.items.filter {
                (it as ItemModel).name.unaccent().contains(searchText.unaccent(), true)
            }

        loadItems(GenericList(TabTypeEnum.ITEM, searchedItems))
    }

    fun searchVolunteers(searchText: String) = volunteersList?.let { list ->
        val searchedItems =
            list.items.filter { (it as VolunteerModel).name.unaccent().contains(searchText.unaccent(), true) }

        loadItems(GenericList(TabTypeEnum.VOLUNTEER, searchedItems))
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadItems(list: GenericList?) {
        list?.let { this.items = it }
        notifyDataSetChanged()
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = WarehouseControlListItemBinding.bind(view)
    }
}