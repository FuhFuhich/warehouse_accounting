package com.example.warehouse_accounting.ui.products

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.models.Product

class ProductAdapter(
    private val products: List<Product>,
    private val fabHelper: ProductLongClickHelper
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_product_name)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        val tvBarcode: TextView = itemView.findViewById(R.id.tv_barcode)
        val tvQuantity: TextView = itemView.findViewById(R.id.tv_quantity)
        val ivProduct: ImageView = itemView.findViewById(R.id.iv_product_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_products_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.tvName.text = product.name
        holder.tvDescription.text = product.description
        holder.tvBarcode.text = product.barcode
        holder.tvQuantity.text = product.quantity.toString()

        holder.itemView.setOnLongClickListener {
            fabHelper.showOptionsDialog { selectedOption ->
                fabHelper.showToast(selectedOption)
            }
            true
        }
    }

    override fun getItemCount(): Int = products.size
}
