package com.example.cursobasico.superheroes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cursobasico.R

class SuperHeroesAdapter(
    var superHeroesList: List<SuperHeroeItemResponse> = emptyList(),
    private val onItemSelected: (String) -> Unit
) : RecyclerView.Adapter<SuperHeroesViewHolder>() {

    fun updateList(superHeroesList: List<SuperHeroeItemResponse>) {
        this.superHeroesList = superHeroesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = SuperHeroesViewHolder(
        LayoutInflater.from(parent.context).inflate(R.layout.item_super_heroe, parent, false)
    )

    override fun onBindViewHolder(holder: SuperHeroesViewHolder, position: Int) {
        holder.bind(superHeroesList[position], onItemSelected)
    }

    override fun getItemCount() = superHeroesList.size
}