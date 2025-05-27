package com.example.warehouse_accounting.ui.warehouses

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.models.Warehouses

class WarehousesAdapter(
    private var warehouses: MutableList<Warehouses>,
    private val longClickHelper: WarehousesLongClickHelper,
    private val editWarehousesCallback: (Warehouses) -> Unit,
    private val deleteWarehousesCallback: (Warehouses) -> Unit
) : RecyclerView.Adapter<WarehousesAdapter.WarehousesViewHolder>() {

    inner class WarehousesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_warehouses_name)
        val tvQuantity: TextView = itemView.findViewById(R.id.tv_warehouse_quantity) // ДОБАВИЛИ
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarehousesViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_warehouses_item, parent, false)
        return WarehousesViewHolder(view)
    }

    override fun onBindViewHolder(holder: WarehousesViewHolder, position: Int) {
        val warehouse = warehouses[position]
        holder.tvName.text = warehouse.warehousesName

        holder.tvQuantity.text = if (warehouse.totalQuantity > 0) {
            warehouse.totalQuantity.toString()
        } else {
            "0"
        }

        holder.itemView.setOnClickListener {
            editWarehousesCallback(warehouse)
        }

        holder.itemView.setOnLongClickListener {
            longClickHelper.showOptionsDialog(
                warehouse = warehouse,
                onDelete = { deleteWarehousesCallback(warehouse) }
            )
            true
        }
    }

    override fun getItemCount(): Int = warehouses.size

    fun updateWarehouses(newWarehouses: MutableList<Warehouses>) {
        warehouses = newWarehouses
        notifyDataSetChanged()
    }
}
