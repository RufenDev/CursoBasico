package com.example.cursobasico.imc

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.cursobasico.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.slider.RangeSlider
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow

class IMCAppActivity : AppCompatActivity() {

    private fun navigateResult(result:Double){
        val intentResult = Intent(this, IMCResultActivity::class.java)
        intentResult.putExtra(IMC_KEY, result)
        startActivity(intentResult)
    }

    private fun calcularIMC():Double {
        val imc = pesoActual / (alturaActual.toDouble() / 100).pow(2)
        return BigDecimal(imc)
            .setScale(2, RoundingMode.HALF_EVEN)
            .toDouble()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_imcapp)

        initComponents()
        initListeners()
        initUI()
    }

    //INICIALIZADORES
    private fun initComponents() {
        //Obtener los elementos
        cardHombre = findViewById(R.id.cardHombre)
        tvHombre = findViewById(R.id.tvHombre)
        imgHombre = findViewById(R.id.imgHombre)

        cardMujer = findViewById(R.id.cardMujer)
        tvMujer = findViewById(R.id.tvMujer)
        imgMujer = findViewById(R.id.imgMujer)

        tvAltura = findViewById(R.id.tvAltura)
        rsAltura = findViewById(R.id.rsAltura)

        txtEdad = findViewById(R.id.txtEdad)
        btnSumarEdad = findViewById(R.id.btnSumarEdad)
        btnRestarEdad = findViewById(R.id.btnRestarEdad)

        txtPeso = findViewById(R.id.txtPeso)
        btnSumarPeso = findViewById(R.id.btnSumarPeso)
        btnRestarPeso = findViewById(R.id.btnRestarPeso)

        btnCalcular = findViewById(R.id.btnCalcular)
    }

    private fun initListeners() {
        cardHombre.setOnClickListener {
            gender = true //Hombre
            setGenderColor()
        }

        cardMujer.setOnClickListener {
            gender = false //Mujer
            setGenderColor()
        }

        rsAltura.addOnChangeListener { _, value, _ ->
            setAltura(value)
        }

        btnSumarEdad.setOnClickListener {
            sumarEdad()
            setEdad()
        }
        btnRestarEdad.setOnClickListener {
            restarEdad()
            setEdad()
        }

        //Listener para el EditText
        txtPeso.addTextChangedListener { text ->
            //La variable text tiene como valor el texto dentro del EditText
            //Convertir el texto a entero y guardarlo como el peso actual, si
            // el edittext se encuentra vacío, se asigna el valor 0
           pesoActual = text?.toString()?.toIntOrNull() ?: 0
        }
        btnSumarPeso.setOnClickListener {
            sumarPeso()
            setPeso()
        }
        btnRestarPeso.setOnClickListener {
            restarPeso()
            setPeso()
        }

        btnCalcular.setOnClickListener {
            navigateResult(calcularIMC())
        }
    }

    private fun initUI(){
        setGenderColor()
        setPeso()
        setEdad()
    }

    //METODOS
    @SuppressLint("SetTextI18n")
    private fun setAltura(value: Float) {
        alturaActual = value.toInt()
        tvAltura.text = "$alturaActual cm"
    }

    private fun setPeso(){
        txtPeso.setText(pesoActual.toString())
    }

    private fun setEdad(){
        txtEdad.setText(edadActual.toString())
    }

    private fun restarPeso() {
        if(pesoActual > 1){
            pesoActual--
        }
    }

    private fun sumarPeso() {
        if(pesoActual < 300){
            pesoActual++
        }
    }

    private fun restarEdad() {
        if(edadActual > 1){
            edadActual--
        }
    }

    private fun sumarEdad() {
        if(edadActual < 110){
            edadActual++
        }
    }

    private fun setGenderColor() {
        var colors = getColors(gender)
        cardHombre.setCardBackgroundColor(colors[0])
        tvHombre.setTextColor(colors[1])
        imgHombre.setColorFilter(colors[1])

        colors = getColors(!gender)
        cardMujer.setCardBackgroundColor(colors[0])
        tvMujer.setTextColor(colors[1])
        imgMujer.setColorFilter(colors[1])
    }

    private fun getColors(isSelected: Boolean) : Array<Int>{
        val txt:Int
        val bg:Int

        if(isSelected){
            txt = R.color.white
            bg = R.color.imc_btn

        } else {
            txt = R.color.imc_txt_color
            bg = R.color.imc_comp
        }

        return arrayOf(ContextCompat.getColor(this, bg), ContextCompat.getColor(this, txt))
    }

    //ATRIBUTOS
    private var gender: Boolean = true //True = Hombre, False = Mujer
    private var pesoActual: Int = 50
    private var edadActual: Int = 20
    private var alturaActual: Int = 120

    //COMPONENTES
    private lateinit var cardHombre: CardView
    private lateinit var tvHombre: AppCompatTextView
    private lateinit var imgHombre: AppCompatImageView

    private lateinit var cardMujer: CardView
    private lateinit var tvMujer: AppCompatTextView
    private lateinit var imgMujer: AppCompatImageView

    private lateinit var tvAltura: AppCompatTextView
    private lateinit var rsAltura: RangeSlider

    private lateinit var txtEdad: AppCompatEditText
    private lateinit var btnSumarEdad: FloatingActionButton
    private lateinit var btnRestarEdad: FloatingActionButton

    private lateinit var txtPeso: AppCompatEditText
    private lateinit var btnSumarPeso: FloatingActionButton
    private lateinit var btnRestarPeso: FloatingActionButton

    private lateinit var btnCalcular: AppCompatButton

    companion object{
        const val IMC_KEY = "IMC_RESULT"
    }
}