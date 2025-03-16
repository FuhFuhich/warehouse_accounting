package com.example.warehouse_accounting.ui.buyers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.models.Buyers

class BuyersAdapter(
    private var buyers: MutableList<Buyers>,
    private val longClickHelper: BuyersLongClickHelper,
    private val editBuyersCallback: (Buyers) -> Unit
) : RecyclerView.Adapter<BuyersAdapter.BuyersViewHolder>() {

    inner class BuyersViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_buyers_name)
        val tvPhone: TextView = itemView.findViewById(R.id.tv_buyers_phone)
        val tvEmail: TextView = itemView.findViewById(R.id.tv_buyers_email)
        val tvNote: TextView = itemView.findViewById(R.id.tv_buyers_note)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyersViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_buyers_item, parent, false)
        return BuyersViewHolder(view)
    }

    override fun onBindViewHolder(holder: BuyersViewHolder, position: Int) {
        val buyers = buyers[position]
        holder.tvName.text = buyers.name
        holder.tvPhone.text = buyers.phone
        holder.tvEmail.text = buyers.email
        holder.tvNote.text = buyers.note

        holder.itemView.setOnClickListener {
            editBuyersCallback(buyers)
        }

        holder.itemView.setOnLongClickListener {
            longClickHelper.showOptionsDialog { selectedOption ->
                longClickHelper.showToast(selectedOption)
            }
            true
        }
    }

    override fun getItemCount(): Int = buyers.size

    fun updateBuyers(newBuyers: MutableList<Buyers>) {
        buyers = newBuyers
        notifyDataSetChanged()
    }
}
