package com.example.cursobasico.superheroes

import android.app.ActionBar.LayoutParams
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.example.cursobasico.databinding.ActivitySuperHeroesDetailsBinding
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.math.roundToInt

class SuperHeroesDetailsActivity : AppCompatActivity() {

    companion object {
        const val HEROE_KEY = "heroe_id"
    }

    private lateinit var binding: ActivitySuperHeroesDetailsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuperHeroesDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id: String = intent.getStringExtra(HEROE_KEY).orEmpty()
        getSuperHeroeInformation(id)
    }

    private fun getSuperHeroeInformation(id: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val superHeroeDetails: Response<SuperHeroeDetailsResponse> =
                getRetroFit().create(ApiService::class.java).getSuperHeroeDetail(id)

            if (superHeroeDetails.body() != null) {
                runOnUiThread {
                    initUI(superHeroeDetails.body()!!)
                }
            }
        }
    }

    private fun initUI(superHeroe: SuperHeroeDetailsResponse) {
        Picasso.get().load(superHeroe.image.url).into(binding.ivSuperHeroe)

        binding.tvSuperHeroeNameDetails.text = superHeroe.name

        setStats(superHeroe.powerStats)
        setDetails(superHeroe.biography, superHeroe.appearance)
    }

    private fun setDetails(biography: SuperHeroeBiography, appearance: SuperHeroeAppearance) {
        binding.tvSuperHeroeRealName.text = biography.fullName
        binding.tvSuperHeroePublisher.text = biography.publisher

        binding.tvSuperHeroeGender.text = appearance.gender
        binding.tvSuperHeroeRace.text = appearance.race
        binding.tvSuperHeroeHeight.text = appearance.height[1]
        binding.tvSuperHeroeWeight.text = appearance.weight[1]
    }

    private fun setStats(powerStats: SuperHeroePowerStats) {
        updateStatusBar(
            binding.tvInteligencia,
            powerStats.intelligence,
            binding.viewInteligenciaMargin
        )
        updateStatusBar(
            binding.tvFuerza,
            powerStats.strength,
            binding.viewFuerzaMargin
        )
        updateStatusBar(
            binding.tvVelocidad,
            powerStats.speed,
            binding.viewVelocidadMargin
        )
        updateStatusBar(
            binding.tvDurabilidad,
            powerStats.durability,
            binding.viewDurabilidadMargin
        )
        updateStatusBar(
            binding.tvPoder,
            powerStats.power,
            binding.viewPoderMargin
        )
        updateStatusBar(
            binding.tvCombate,
            powerStats.combat,
            binding.viewCombateMargin
        )
    }

    private fun updateStatusBar(bar: TextView, stat: String, margin: View) {
        val statusBar: Float = stat.toFloatOrNull() ?: 0f
        bar.layoutParams = getWeight(statusBar / 100)
        bar.text = statusBar.roundToInt().toString()

        val marginWeight = 100 - statusBar
        margin.layoutParams = getWeight(marginWeight / 100)
    }

    private fun getWeight(weight: Float) =
        LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, weight)

    private fun getRetroFit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://superheroapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}