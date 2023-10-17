package com.example.cursobasico.saludo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import com.example.cursobasico.R

class FirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Obtener el edit text
        val etNombre = findViewById<AppCompatEditText>(R.id.etNombre)

        //Obtener el botón
        val btnPresioname = findViewById<AppCompatButton>(R.id.btnPresioname)

        //Click Listener
        btnPresioname.setOnClickListener {
            val intento = Intent(this, SecondActivity::class.java);
            intento.putExtra("NOMBRE", etNombre.text.toString())
            startActivity(intento)
        }
    }
}