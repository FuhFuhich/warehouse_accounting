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
    private val editSuppliersCallback: (Suppliers) -> Unit
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
        val suppliers = suppliers[position]
        holder.tvName.text = suppliers.name
        holder.tvPhone.text = suppliers.phone
        holder.tvEmail.text = suppliers.email
        holder.tvNote.text = suppliers.note

        holder.itemView.setOnClickListener {
            editSuppliersCallback(suppliers)
        }

        holder.itemView.setOnLongClickListener {
            longClickHelper.showOptionsDialog { selectedOption ->
                longClickHelper.showToast(selectedOption)
            }
            true
        }
    }

    override fun getItemCount(): Int = suppliers.size

    fun updateSuppliers(newSuppliers: MutableList<Suppliers>) {
        suppliers = newSuppliers
        notifyDataSetChanged()
    }
}
