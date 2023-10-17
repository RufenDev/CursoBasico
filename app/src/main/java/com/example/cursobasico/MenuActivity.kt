package com.example.cursobasico

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import com.example.cursobasico.imc.IMCAppActivity
import com.example.cursobasico.recyclerviewapp.AnimeActivity
import com.example.cursobasico.saludo.FirstActivity
import com.example.cursobasico.settings.SettingsActivity
import com.example.cursobasico.superheroes.SuperHeroesListActivity
import com.example.cursobasico.todo.TodoActivity

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        //Primer aplicación
        val btnSaludo:AppCompatButton = findViewById(R.id.btnSaludo)
        btnSaludo.setOnClickListener { navigateToSaludo() }

        //Segunda aplicación
        val btnIMC:AppCompatButton = findViewById(R.id.btnIMCApp)
        btnIMC.setOnClickListener { navigateToIMC() }

        //Tercera aplicación
        val btnTODO:AppCompatButton = findViewById(R.id.btnTODO)
        btnTODO.setOnClickListener { navigateToTODO() }

        //Cuarta aplicación
        val btnSuperHeroes:AppCompatButton = findViewById(R.id.btnSuperHeroes)
        btnSuperHeroes.setOnClickListener { navigateToSuperHeroes() }

        //Cuarta aplicación
        val btnSettings:AppCompatButton = findViewById(R.id.btnSettings)
        btnSettings.setOnClickListener { navigateToSettings() }

        //RecyclerView App
        val btnRecyclerView:AppCompatButton = findViewById(R.id.btnRecyclerView)
        btnRecyclerView.setOnClickListener { navigateToRVA() }
    }

    private fun navigateToTODO() {
        val intento = Intent(this, TodoActivity::class.java)
        startActivity(intento)
    }

    private fun navigateToIMC() {
        val intento = Intent(this, IMCAppActivity::class.java)
        startActivity(intento)
    }

    private fun navigateToSaludo() {
        val intento = Intent(this, FirstActivity::class.java)
        startActivity(intento)
    }

    private fun navigateToSuperHeroes(){
        val intento = Intent(this, SuperHeroesListActivity::class.java)
        startActivity(intento)

    }

    private fun navigateToSettings(){
        val intento = Intent(this, SettingsActivity::class.java)
        startActivity(intento)

    }

    private fun navigateToRVA(){
        val intento = Intent(this, AnimeActivity::class.java)
        startActivity(intento)

    }
}