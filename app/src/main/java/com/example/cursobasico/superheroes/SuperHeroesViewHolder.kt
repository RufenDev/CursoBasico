package com.example.cursobasico.superheroes

import android.annotation.SuppressLint
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.cursobasico.databinding.ItemSuperHeroeBinding
import com.squareup.picasso.Picasso

class SuperHeroesViewHolder(view:View) : RecyclerView.ViewHolder(view){

    private val binding = ItemSuperHeroeBinding.bind(view)

    @SuppressLint("SetTextI18n")
    fun bind(superHeroeItemResponse: SuperHeroeItemResponse, onItemSelected: (String) -> Unit){
        binding.tvSuperHeroeName.text = superHeroeItemResponse.heroe_name
        Picasso.get().load(superHeroeItemResponse.heroe_img.img_url).into(binding.ivSuperHeroeImage)

        binding.root.setOnClickListener{onItemSelected(superHeroeItemResponse.heroe_id)}
    }

}