package com.example.cursobasico.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cursobasico.R

class CategoriasAdapter(private val categorias: List<Categorias>, private val onCategoriaSelected: (Int) -> Unit) :
    RecyclerView.Adapter<CategoriasViewHolder>() {

    override fun getItemCount() = categorias.size

    override fun onBindViewHolder(holder: CategoriasViewHolder, position: Int) {
        holder.render(categorias[position], onCategoriaSelected)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriasViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_categorias, parent, false)
        return CategoriasViewHolder(view)
    }
}