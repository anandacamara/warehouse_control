package org.jw.warehousecontrol.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.jw.warehousecontrol.R
import org.jw.warehousecontrol.databinding.WarehouseControlReturnListItemBinding
import org.jw.warehousecontrol.presentation.model.GenericListItem
import org.jw.warehousecontrol.presentation.model.delegate.OnReturnItemDelegate

/**
 * @author Ananda Camara
 */
internal class BorrowedItemAdapter(
    private val delegate: OnReturnItemDelegate,
    private val items: MutableList<GenericListItem>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.warehouse_control_return_list_item, parent, false)

        return ReturnItemViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        with(holder as ReturnItemViewHolder) {
            val item = items[position]
            holder.binding.itemName.text = item.name
            holder.binding.returnIcon.setOnClickListener { delegate.onReturnItem(position) }
        }

    fun addNewItem(item: GenericListItem) {
        items.add(item)
        notifyItemInserted(items.lastIndex)
        notifyItemRangeChanged(items.lastIndex, items.size)
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
    }

    fun getItem(position: Int) = items[position]

    inner class ReturnItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = WarehouseControlReturnListItemBinding.bind(view)
    }
}