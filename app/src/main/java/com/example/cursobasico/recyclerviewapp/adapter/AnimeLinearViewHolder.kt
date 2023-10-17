package com.example.cursobasico.recyclerviewapp.adapter

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.cursobasico.R
import com.example.cursobasico.databinding.ItemCharacterLinearDisplayBinding
import com.example.cursobasico.recyclerviewapp.AnimeActivity
import com.example.cursobasico.recyclerviewapp.AnimeCharacter
import com.squareup.picasso.Picasso

class AnimeLinearViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemCharacterLinearDisplayBinding.bind(view)
    private var onPhotoClickListener:((Int, Bitmap) -> Unit)? = null
    private var onItemSelected:((Int, Boolean) -> Unit)? = null
    private var moreInformation:((Int) -> Unit)? = null

    fun render(
        animeCharacter: AnimeCharacter,
        onPhotoClickListener: (Int, Bitmap) -> Unit,
        onItemSelected: (Int, Boolean) -> Unit,
        moreInformation: (Int) -> Unit
    ) {

        this.onPhotoClickListener = onPhotoClickListener
        this.onItemSelected = onItemSelected
        this.moreInformation = moreInformation

        binding.tvLinearName.text = animeCharacter.name
        binding.tvLinearAnime.text = animeCharacter.anime
        selectedBg(animeCharacter.isSelected)

        val path: String = animeCharacter.photoURL.ifEmpty {
            //Unknown protagonist photo
            "https://ih1.redbubble.net/image.1380092756.9137/raf,360x360,075,t,fafafa:ca443f4786.jpg"
        }
        Picasso.get().load(path).into(binding.imgLinearPhoto)

        itemView.setOnLongClickListener {
            longPress(animeCharacter.isSelected)
        }
        binding.imgLinearPhoto.setOnLongClickListener {
            longPress(animeCharacter.isSelected)
        }

        itemView.setOnClickListener {
            normalPress(animeCharacter.isSelected, AnimeActivity.isSelectedState)
        }

        binding.imgLinearPhoto.setOnClickListener {
            normalPress(animeCharacter.isSelected, AnimeActivity.isSelectedState, true)
        }
    }

    private fun normalPress(isSelected:Boolean, isSelectedState:Boolean, openPhoto:Boolean=false) {
        if(isSelected){
            onItemSelected?.invoke(adapterPosition, false)
            selectedBg(false)
        }
        else if(isSelectedState){
            onItemSelected?.invoke(adapterPosition, true)
            selectedBg(true)

        } else if (openPhoto){
            val photo = (binding.imgLinearPhoto.drawable as BitmapDrawable).bitmap
            onPhotoClickListener?.invoke(adapterPosition, photo)

        } else {
            moreInformation?.invoke(adapterPosition)
        }
    }

    private fun longPress(selected:Boolean):Boolean {
        if (!selected) {
            onItemSelected?.invoke(adapterPosition, true)
            selectedBg(true)
        }
        return true
    }

    fun selectedBg(isSelected: Boolean) {
        binding.cvBackground.setCardBackgroundColor(
            ContextCompat.getColor(
                itemView.context,
                if (isSelected) R.color.recyclerapp_bg_selected
                else android.R.color.transparent
            )
        )
    }

}