package org.jw.warehousecontrol.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.jw.warehousecontrol.R
import org.jw.warehousecontrol.databinding.WarehouseControlBorrowListItemBinding
import org.jw.warehousecontrol.presentation.model.UIItem
import org.jw.warehousecontrol.presentation.model.UIItemReference
import org.jw.warehousecontrol.presentation.model.delegate.EmptyListNotificationDelegate

/**
 * @author Ananda Camara
 */
internal class LendItemAdapter(
    private val delegate: EmptyListNotificationDelegate
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val items = mutableListOf<UIItemReference>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.warehouse_control_borrow_list_item, parent, false)

        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        with(holder as ItemViewHolder) {
            val item = items[position]

            binding.icon.setImageResource(item.uiItem.defaultResourceId)
            binding.itemName.text = item.uiItem.name
            binding.itemCount.text = ITEM_COUNT.format(item.count)

            binding.minusButton.setOnClickListener {
                decreaseItem(position)
                if (items.isEmpty()) delegate.notifyEmptyList()
            }

            binding.plusButton.setOnClickListener {
                increaseItem(position)
            }
        }

    fun addItem(uiItem: UIItem) {
        when(val index = items.indexOfFirst { it.uiItem.name == uiItem.name }) {
            -1 -> {
                items.add(UIItemReference(uiItem,  1))
                notifyItemInserted(items.lastIndex)
                notifyItemRangeChanged(items.lastIndex, items.size)
            }
            else -> increaseItem(index)
        }
    }

    private fun removeItem(position: Int) {
        items.removeAt(position)

        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
    }

    private fun decreaseItem(position: Int) {
        if (items[position].count == 1) removeItem(position)
        else {
            items[position].count--
            notifyItemChanged(position)
        }
    }

    private fun increaseItem(position: Int) {
        items[position].count++

        notifyItemChanged(position)
    }

    inner class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = WarehouseControlBorrowListItemBinding.bind(view)
    }

    companion object {
        private const val ITEM_COUNT = "Quantidade: %s"
    }
}