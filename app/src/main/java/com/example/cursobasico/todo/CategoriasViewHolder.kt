package com.example.cursobasico.todo

import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cursobasico.R

class CategoriasViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun render(categoria: Categorias, onCategoriaSelected: (Int) -> Unit) {

        itemView.setOnClickListener {
            onCategoriaSelected(layoutPosition)
        }

        cvContainerCategoria.setCardBackgroundColor(
            ContextCompat.getColor(
                cvContainerCategoria.context,
                if (categoria.isSelected) {
                    R.color.todo_component
                } else {
                    R.color.todo_disable_component
                }
            )
        )

        tvCategoriaNombre.text = when(categoria){
            Categorias.Business -> "Negocio"
            Categorias.Personal -> "Personal"
            else -> "Otros"
        }
        tvCategoriaNombre.setTextColor(
            ContextCompat.getColor(
                tvCategoriaNombre.context,
                if (categoria.isSelected) {
                    R.color.white
                } else {
                    R.color.todo_disable_component_elements
                }
            )
        )

        val separadorColor = if(categoria.isSelected){
            when(categoria){
                Categorias.Business -> R.color.imc_btn
                Categorias.Personal -> R.color.imc_green
                else -> R.color.todo_yellow
            }
        } else {
            R.color.todo_disable_component_elements
        }
        separador.setBackgroundColor(
            ContextCompat.getColor(separador.context, separadorColor)
        )
    }

    //COMPONENTES
    private val tvCategoriaNombre: AppCompatTextView = view.findViewById(R.id.tvCategoriaNombre)
    private val cvContainerCategoria: CardView = view.findViewById(R.id.cvContainerCategoria)
    private val separador: View = view.findViewById(R.id.separador)


}