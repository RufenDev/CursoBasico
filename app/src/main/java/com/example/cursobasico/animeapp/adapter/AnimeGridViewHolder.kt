package com.example.cursobasico.animeapp.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.cursobasico.databinding.ItemCharacterGridDisplayBinding
import com.example.cursobasico.animeapp.AnimeActivity
import com.example.cursobasico.animeapp.AnimeCharacter
import com.squareup.picasso.Picasso

class AnimeGridViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val binding = ItemCharacterGridDisplayBinding.bind(view)

    @SuppressLint("ClickableViewAccessibility")
    fun render(
        animeCharacter: AnimeCharacter,
        onPhotoClickListener: (Int, Bitmap) -> Unit,
        onItemSelected: (Int, Boolean) -> Unit
    ) {

        binding.tvGridName.text = animeCharacter.name
        binding.viewGridShadow.visibility = if(animeCharacter.isSelected) View.VISIBLE else View.GONE

        val path: String = animeCharacter.photoURL.ifEmpty {
            //Unknown protagonist photo
            "https://ih1.redbubble.net/image.1380092756.9137/raf,360x360,075,t,fafafa:ca443f4786.jpg"
        }
        Picasso.get().load(path).into(binding.imgGridPhoto)

        itemView.setOnLongClickListener {
            if (!animeCharacter.isSelected) {
                onItemSelected(adapterPosition, true)
                binding.viewGridShadow.visibility = View.VISIBLE
            }
            true
        }
        itemView.setOnClickListener {
            if(animeCharacter.isSelected){
                onItemSelected(adapterPosition, false)
                binding.viewGridShadow.visibility = View.GONE

            } else if (AnimeActivity.isSelectedState){
                onItemSelected(adapterPosition, true)
                binding.viewGridShadow.visibility = View.VISIBLE

            } else {
                val photo: Bitmap = (binding.imgGridPhoto.drawable as BitmapDrawable).bitmap
                onPhotoClickListener(adapterPosition, photo)
            }
        }

    }
}