package com.example.warehouse_accounting.ui.suppliers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.models.Suppliers

class SuppliersAdapter(
    private var suppliers: MutableList<Suppliers>,
    private val longClickHelper: SuppliersLongClickHelper,
    private val editSuppliersCallback: (Suppliers) -> Unit,
    private val deleteSuppliersCallback: (Suppliers) -> Unit
) : RecyclerView.Adapter<SuppliersAdapter.SuppliersViewHolder>() {

    inner class SuppliersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_suppliers_name)
        val tvPhone: TextView = itemView.findViewById(R.id.tv_suppliers_phone)
        val tvEmail: TextView = itemView.findViewById(R.id.tv_suppliers_email)
        val tvNote: TextView = itemView.findViewById(R.id.tv_suppliers_note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuppliersViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_suppliers_item, parent, false)
        return SuppliersViewHolder(view)
    }

    override fun onBindViewHolder(holder: SuppliersViewHolder, position: Int) {
        val supplier = suppliers[position]
        holder.tvName.text = supplier.name
        holder.tvPhone.text = supplier.phone ?: ""
        holder.tvEmail.text = supplier.email ?: ""
        holder.tvNote.text = supplier.note ?: ""

        holder.itemView.setOnClickListener {
            editSuppliersCallback(supplier)
        }

        holder.itemView.setOnLongClickListener {
            longClickHelper.showOptionsDialog(
                supplier = supplier,
                onDelete = { deleteSuppliersCallback(supplier) }
            )
            true
        }
    }

    override fun getItemCount(): Int = suppliers.size

    fun updateSuppliers(newSuppliers: MutableList<Suppliers>) {
        suppliers = newSuppliers
        notifyDataSetChanged()
    }
}
