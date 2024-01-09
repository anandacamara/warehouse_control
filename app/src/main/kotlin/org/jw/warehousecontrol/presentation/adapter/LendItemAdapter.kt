package org.jw.warehousecontrol.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.jw.warehousecontrol.R
import org.jw.warehousecontrol.databinding.WarehouseControlListItemBinding
import org.jw.warehousecontrol.presentation.model.ItemModel
import org.jw.warehousecontrol.presentation.model.delegate.EmptyListNotificationDelegate

/**
 * @author Ananda Camara
 */
internal class LendItemAdapter(
    private val delegate: EmptyListNotificationDelegate
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val items = mutableListOf<ItemModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.warehouse_control_list_item, parent, false)

        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        with(holder as ItemViewHolder) {
            val item = items[position]

            binding.icon.setImageResource(item.defaultResourceId)
            holder.binding.itemName.text = item.name
            holder.binding.closeButton.visibility = View.VISIBLE

            holder.binding.closeButton.setOnClickListener {
                removeItem(position)
                if (items.isEmpty()) delegate.notifyEmptyList()
            }
        }

    fun addItem(item: ItemModel) {
        items.add(item)

        notifyItemInserted(items.lastIndex)
        notifyItemRangeChanged(items.lastIndex, items.size)
    }

    private fun removeItem(position: Int) {
        items.removeAt(position)

        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = WarehouseControlListItemBinding.bind(view)
    }
}