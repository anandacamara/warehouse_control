package org.jw.warehousecontrol.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.jw.warehousecontrol.R
import org.jw.warehousecontrol.databinding.WarehouseControlReturnListItemBinding
import org.jw.warehousecontrol.presentation.model.UIItemReference
import org.jw.warehousecontrol.presentation.model.delegate.OnReturnItemDelegate

/**
 * @author Ananda Camara
 */
internal class BorrowedItemAdapter(
    private val delegate: OnReturnItemDelegate,
    private val items: MutableList<UIItemReference>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val allItemsCount: Int get() = items.sumOf { it.count }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.warehouse_control_return_list_item, parent, false)

        return ReturnItemViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        with(holder as ReturnItemViewHolder) {
            val item = items[position]
            binding.itemName.text = item.uiItem.name
            binding.itemCount.text = ITEM_COUNT.format(item.count)
            binding.returnIcon.setOnClickListener { delegate.onReturnItem(position) }
        }

    fun addNewItem(reference: UIItemReference) {
        when (val existingItem = items.firstOrNull { it.uiItem.name == reference.uiItem.name }) {
            null -> items.add(reference)
            else -> existingItem.count += reference.count
        }

        notifyItemInserted(items.lastIndex)
        notifyItemRangeChanged(items.lastIndex, items.size)
    }

    fun removeItem(position: Int) {
        if (items[position].count > 1) {
            items[position].count--
            notifyItemChanged(position)
        } else {
            items.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, items.size)
        }
    }

    fun getItem(position: Int) = items[position]

    inner class ReturnItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = WarehouseControlReturnListItemBinding.bind(view)
    }

    companion object {
        private const val ITEM_COUNT = "Quantidade: %s"
    }
}