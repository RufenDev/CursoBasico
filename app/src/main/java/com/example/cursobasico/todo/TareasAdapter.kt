package com.example.cursobasico.todo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cursobasico.R

class TareasAdapter(var tareas: List<Tareas>, private val onTareaSelected: (Int) -> Unit) : RecyclerView.Adapter<TareasViewHolder>() {

    override fun getItemCount() = tareas.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareasViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tarea, parent, false)
        return TareasViewHolder(view)
    }

    override fun onBindViewHolder(holder: TareasViewHolder, position: Int) {
        holder.render(tareas[position])
        holder.itemView.setOnClickListener{
            onTareaSelected(position)
        }
    }
}