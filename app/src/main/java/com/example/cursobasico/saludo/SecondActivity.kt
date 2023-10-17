package com.example.cursobasico.saludo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatTextView
import com.example.cursobasico.R

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        //Obtener el textView
        val saludo = findViewById<AppCompatTextView>(R.id.tvSaludo)

        //Obtener el nombre del Intent
        val nombre:String = intent.extras?.getString("NOMBRE").orEmpty()

        saludo.text = "Hola, $nombre"

    }
}