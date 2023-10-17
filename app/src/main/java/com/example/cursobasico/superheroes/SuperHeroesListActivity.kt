package com.example.cursobasico.superheroes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cursobasico.databinding.ActivitySuperHeroesListBinding
import com.example.cursobasico.superheroes.SuperHeroesDetailsActivity.Companion.HEROE_KEY
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SuperHeroesListActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySuperHeroesListBinding
    private lateinit var retrofit: Retrofit
    private lateinit var adapter: SuperHeroesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuperHeroesListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrofit = getRetroFit()

        initUI()
    }

    private fun initUI() {
        binding.svSuperHeroes.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchByName(query.orEmpty())
                return false
            }

            override fun onQueryTextChange(newText: String?) = false
        })

        adapter = SuperHeroesAdapter{navigateToDetails(it)}
        binding.rcvSuperHeroes.setHasFixedSize(true)
        binding.rcvSuperHeroes.layoutManager = LinearLayoutManager(this)
        binding.rcvSuperHeroes.adapter = adapter
    }

    private fun searchByName(query: String) {
        binding.pbSuperHeroes.isVisible = true

        CoroutineScope(Dispatchers.IO).launch {
            val myResponse: Response<SuperHeroeDataResponse> = retrofit.create(ApiService::class.java).getSuperHeroes(query)

            if(myResponse.isSuccessful){

                Log.i("Conexion_API", "Conexión exitosa :)")

                val response: SuperHeroeDataResponse? = myResponse.body()

                runOnUiThread {
                    if (response != null) {
                        adapter.updateList(response.superheroes)
                    }
                    binding.pbSuperHeroes.isVisible = false
                }

            } else {
                Log.i("Conexion_API", "No se conectó.")

            }
        }
    }

    private fun getRetroFit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://superheroapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun navigateToDetails(id:String){
        val intent = Intent(this, SuperHeroesDetailsActivity::class.java)
        intent.putExtra(HEROE_KEY, id)
        startActivity(intent)
    }


}