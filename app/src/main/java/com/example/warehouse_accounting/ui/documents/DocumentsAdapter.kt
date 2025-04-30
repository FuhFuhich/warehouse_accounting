package com.example.warehouse_accounting.ui.documents

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.warehouse_accounting.R
import com.example.warehouse_accounting.models.Documents

class DocumentsAdapter(
    private var documents: MutableList<Documents>,
    private val longClickHelper: DocumentsLongClickHelper,
    private val editDocumentsCallback: (Documents) -> Unit
) : RecyclerView.Adapter<DocumentsAdapter.DocumentsViewHolder>() {

    inner class DocumentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDocuments: TextView = itemView.findViewById(R.id.tv_documents_document)
        val tvDocumentsCreationDate: TextView = itemView.findViewById(R.id.tv_documents_creation_date)
        val tvDocumentsNameOfWarehouse: TextView = itemView.findViewById(R.id.tv_documents_name_of_warehouse)
        val tvDocumentsQuantityOfGoods: TextView = itemView.findViewById(R.id.tv_documents_quantity_of_goods)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_documents_item, parent, false)
        return DocumentsViewHolder(view)
    }

    override fun onBindViewHolder(holder: DocumentsViewHolder, position: Int) {
        val documents = documents[position]
        holder.tvDocuments.text = documents.documentsNumber
        holder.tvDocumentsCreationDate.text = documents.creationDate
        holder.tvDocumentsNameOfWarehouse.text = documents.nameOfWarehouse
        holder.tvDocumentsQuantityOfGoods.text = documents.quantityOfGoods.toString()

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
