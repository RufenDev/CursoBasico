package com.example.cursobasico.todo

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cursobasico.R
import com.example.cursobasico.todo.Categorias.*
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TodoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todoactivity)

        initComponents()
        initListeners()
        initUI()
    }

    private fun initComponents() {
        rcvCategorias = findViewById(R.id.rcvCategorias)
        rcvTareas = findViewById(R.id.rcvTareas)
        fabAgregarTarea = findViewById(R.id.fabAgregarTarea)
    }

    private fun initListeners() {
        fabAgregarTarea.setOnClickListener { showDialog() }
    }

    private fun showDialog() {
        val dialogo = Dialog(this)
        dialogo.setContentView(R.layout.dialog_tareas)

        val btnAgregarTarea: AppCompatButton = dialogo.findViewById(R.id.btnAgregarTarea)
        val etNombreCategoria: AppCompatEditText = dialogo.findViewById(R.id.etNombreCategoria)
        val rgCategoria: RadioGroup = dialogo.findViewById(R.id.rgCategoria)

        btnAgregarTarea.setOnClickListener {
            val name:String = etNombreCategoria.text.toString().trim()
            if(name.isNotEmpty()){
                val selectedID: Int = rgCategoria.checkedRadioButtonId
                val selectedRadioButton: RadioButton = rgCategoria.findViewById(selectedID)

                val currentCategoria: Categorias = when (selectedRadioButton.text) {
                    getString(R.string.todo_negocio) -> Business
                    getString(R.string.todo_personal) -> Personal
                    else -> Other
                }

                tareas.add(Tareas(name, currentCategoria))
                updateTareas()
                dialogo.hide()
            }
        }

        dialogo.show()
    }

    private fun initUI() {
        categoriasAdapter = CategoriasAdapter(categorias){updateCategoria(it)}
        rcvCategorias.adapter = categoriasAdapter
        rcvCategorias.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        tareasAdapter = TareasAdapter(tareas){onItemSelected(it)}
        rcvTareas.adapter = tareasAdapter
        rcvTareas.layoutManager = LinearLayoutManager(this)
    }

    private fun updateTareas(){
        val selectedCategoria:List<Categorias> = categorias.filter { it.isSelected }
        val tareasVisibles = tareas.filter { selectedCategoria.contains(it.categoria) }

        tareasAdapter.tareas = tareasVisibles
        tareasAdapter.notifyDataSetChanged()
    }

    private fun updateCategoria(position: Int){
        categorias[position].isSelected = !categorias[position].isSelected
        categoriasAdapter.notifyItemChanged(position)
        updateTareas()
    }

    private fun onItemSelected(position:Int){
        tareas[position].isSelected = !tareas[position].isSelected
        updateTareas()
    }

    //COMPONENTES
    private lateinit var rcvCategorias: RecyclerView
    private lateinit var categoriasAdapter: CategoriasAdapter
    private val categorias = listOf(
        Personal, Business, Other
    )

    private lateinit var rcvTareas: RecyclerView
    private lateinit var tareasAdapter: TareasAdapter
    private val tareas = mutableListOf(
        Tareas("Aprender Android", Business),
        Tareas("Hacer ejercicio", Personal),
        Tareas("Ver una película", Other)
    )

    private lateinit var fabAgregarTarea: FloatingActionButton
}