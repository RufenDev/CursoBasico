package com.example.cursobasico.todo

import android.content.res.ColorStateList
import android.graphics.Paint
import android.view.View
import android.widget.CheckBox
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cursobasico.R

class TareasViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    fun render(tarea: Tareas) {
        tvTarea.text = tarea.name
        cbTarea.isChecked = tarea.isSelected

        tvTarea.paintFlags = if (tarea.isSelected) {
            tvTarea.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            tvTarea.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }

        cbTarea.buttonTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                cbTarea.context,
                when (tarea.categoria) {
                    Categorias.Business -> R.color.imc_btn
                    Categorias.Personal -> R.color.imc_green
                    else -> R.color.todo_yellow
                }
            )
        )
    }

    //COMPONENTES
    private val cbTarea: CheckBox = view.findViewById(R.id.cbTarea)
    private val tvTarea: AppCompatTextView = view.findViewById(R.id.tvTarea)
}