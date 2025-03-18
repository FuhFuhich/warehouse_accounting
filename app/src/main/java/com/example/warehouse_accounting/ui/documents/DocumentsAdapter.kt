package com.example.warehouse_accounting.ui.documents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.models.Documents
import java.util.Date

class DocumentsAdapter(
    private var documents: MutableList<Documents>,
    private val longClickHelper: DocumentsLongClickHelper,
    private val editDocumentsCallback: (Documents) -> Unit
) : RecyclerView.Adapter<DocumentsAdapter.DocumentsViewHolder>() {

    inner class DocumentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDocuments: TextView = itemView.findViewById(R.id.tv_documents)
        val tvDocumentsNumber: TextView = itemView.findViewById(R.id.tv_documents_number)
        val tvCreationDate: TextView = itemView.findViewById(R.id.tv_creation_date)
        val tvNameOfWarehouse: TextView = itemView.findViewById(R.id.tv_name_of_warehouse)
        val tvQuantityOfGoods: TextView = itemView.findViewById(R.id.tv_quantity_of_goods)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_documents_item, parent, false)
        return DocumentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentsViewHolder, position: Int) {
        val documents = documents[position]
        holder.tvDocuments.text = documents.documents
        holder.tvDocumentsNumber.text = documents.documentsNumber
        holder.tvCreationDate.text = documents.creationDate
        holder.tvCreationDate.text = documents.creationDate
        holder.tvNameOfWarehouse.text = documents.nameOfWarehouse
        holder.tvQuantityOfGoods.text = documents.quantityOfGoods.toString()

        holder.itemView.setOnClickListener {
            editDocumentsCallback(documents)
        }

        holder.itemView.setOnLongClickListener {
            longClickHelper.showOptionsDialog { selectedOption ->
                longClickHelper.showToast(selectedOption)
            }
            true
        }
    }

    override fun getItemCount(): Int = documents.size

    fun updateDocuments(newDocuments: MutableList<Documents>) {
        documents = newDocuments
        notifyDataSetChanged()
    }
}
