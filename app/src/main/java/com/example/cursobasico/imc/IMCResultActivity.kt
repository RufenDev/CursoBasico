package com.example.cursobasico.imc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import com.example.cursobasico.R
import com.example.cursobasico.imc.IMCAppActivity.Companion.IMC_KEY

class IMCResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imcresult)

        //Obtener el resultado
        resultIMC = intent.extras?.getDouble(IMC_KEY) ?: -1.0

        initComponents()
        initListeners()
        initUI()
    }

    //INICIALIZADORES
    private fun initComponents() {
        tvResultHealth = findViewById(R.id.tvResultHealth)
        tvResultPercent = findViewById(R.id.tvResultPercent)
        tvResultInfo = findViewById(R.id.tvResultInfo)
        btnRecalcular = findViewById(R.id.btnRecalcular)
    }

    private fun initListeners() {
        btnRecalcular.setOnClickListener { onBackPressed() }
    }

    private fun initUI() {
        //Asignar el valor obtenido
        tvResultPercent.text = resultIMC.toString()

        //Determinar el estado de salud
        when(resultIMC){
            //Peso bajo
            in 0.0 .. 18.49 -> setValues(R.string.imc_title_low, R.string.imc_info_low, R.color.imc_yellow)

            //Peso normal
            in 18.5 .. 24.99 -> setValues(R.string.imc_title_normal, R.string.imc_info_normal, R.color.teal_700)

            //Sobrepeso
            in 25.0 .. 29.99 -> setValues(R.string.imc_title_high, R.string.imc_info_high, R.color.imc_orange)

            //Obesidad
            in 30.0 .. 100.0 -> setValues(R.string.imc_title_over, R.string.imc_info_over, R.color.imc_red)

            //ERROR
            else -> setValues(R.string.imc_error, R.string.imc_error, R.color.imc_red)
        }
    }

    //METODOS
    private fun setValues(health:Int, info:Int, colorReference:Int) {
        //Asignar la información
        tvResultInfo.text = getString(info)

        //Asignar el título y su color
        tvResultHealth.text = getString(health)
        tvResultHealth.setTextColor(getColor(colorReference))

        //Asignar el color del título al botón
        btnRecalcular.backgroundTintList = getColorStateList(colorReference)
        //Determinar si colocar las letras en negro o blanco
        if(colorReference == R.color.imc_yellow){
            btnRecalcular.setTextColor(getColor(R.color.darkgrey))
        } else {
            btnRecalcular.setTextColor(getColor(R.color.white))
        }
    }

    //ATRIBUTOS
    private var resultIMC:Double = 0.0

    //COMPONENTES
    private lateinit var tvResultHealth: AppCompatTextView
    private lateinit var tvResultPercent: AppCompatTextView
    private lateinit var tvResultInfo: AppCompatTextView
    private lateinit var btnRecalcular: AppCompatButton
}