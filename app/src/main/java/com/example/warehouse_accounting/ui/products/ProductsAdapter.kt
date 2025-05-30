package com.example.warehouse_accounting.ui.products

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.models.Product

class ProductsAdapter(
    private var products: MutableList<Product>,
    private val longClickHelper: ProductsLongClickHelper,
    private val editProductCallback: (Product) -> Unit,
    private val deleteProductCallback: (Product) -> Unit
) : RecyclerView.Adapter<ProductsAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tv_product_name)
        val tvDescription: TextView = itemView.findViewById(R.id.tv_description)
        val tvBarcode: TextView = itemView.findViewById(R.id.tv_barcode)
        val tvQuantity: TextView = itemView.findViewById(R.id.tv_quantity)
        val tvWarehouse: TextView = itemView.findViewById(R.id.tv_warehouse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_products_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        Log.d("ProductsAdapter", "Binding position $position: name='${product.name}', desc='${product.description}'")

        holder.tvName.text = product.name.trim().takeIf { it.isNotEmpty() } ?: "Без названия"
        holder.tvDescription.text = product.description?.trim()?.takeIf { it.isNotEmpty() } ?: "Нет описания"
        holder.tvBarcode.text = product.barcode.trim().takeIf { it.isNotEmpty() } ?: "Нет штрих-кода"
        holder.tvQuantity.text = product.quantity.toString()
        holder.tvWarehouse.text = product.warehouse?.trim()?.takeIf { it.isNotEmpty() } ?: "Не указан"

        Log.d("ProductsAdapter", "Set values: name='${holder.tvName.text}', desc='${holder.tvDescription.text}'")

        holder.itemView.setOnClickListener {
            editProductCallback(product)
        }

        holder.itemView.setOnLongClickListener {
            longClickHelper.showOptionsDialog(
                product = product,
                onDelete = { deleteProductCallback(product) }
            )
            true
        }
    }

    override fun getItemCount(): Int = products.size

    fun updateProducts(newProducts: MutableList<Product>) {
        Log.d("ProductsAdapter", "Updating products: ${newProducts.size} items")

        newProducts.forEachIndexed { index, product ->
            Log.d("ProductsAdapter", "Product $index: name='${product.name}', desc='${product.description}'")
        }

        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }

    override fun onViewRecycled(holder: ProductViewHolder) {
        super.onViewRecycled(holder)
        Log.d("ProductsAdapter", "ViewHolder recycled")

        holder.tvName.text = ""
        holder.tvDescription.text = ""
        holder.tvBarcode.text = ""
        holder.tvQuantity.text = ""
        holder.tvWarehouse.text = ""
    }
}
