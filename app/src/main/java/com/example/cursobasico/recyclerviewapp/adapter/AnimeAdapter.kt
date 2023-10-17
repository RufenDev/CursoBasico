package com.example.cursobasico.recyclerviewapp.adapter

import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cursobasico.R
import com.example.cursobasico.recyclerviewapp.AnimeActivity
import com.example.cursobasico.recyclerviewapp.AnimeCharacter

class AnimeAdapter(
    private var charactersList: List<AnimeCharacter>,
    private val onPhotoClickListener: (Int, Bitmap) -> Unit,
    private val onItemSelected: (Int, Boolean) -> Unit,
    private val moreInformation: (Int) -> Unit,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            AnimeActivity.TYPE_LINEAR -> {
                val view = inflater.inflate(R.layout.item_character_linear_display, parent, false)
                AnimeLinearViewHolder(view)
            }
            AnimeActivity.TYPE_GRID -> {
                val view = inflater.inflate(R.layout.item_character_grid_display, parent, false)
                AnimeGridViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(getItemViewType(position)) {
            AnimeActivity.TYPE_LINEAR -> {
                Log.i("DarkModeTest", "LinearViewHolder")
                Log.i("DarkModeTest", "")
                val viewHolder = holder as AnimeLinearViewHolder
                viewHolder.render(charactersList[position], onPhotoClickListener, onItemSelected, moreInformation)

            }
            AnimeActivity.TYPE_GRID -> {
                Log.i("DarkModeTest", "GridViewHolder")
                Log.i("DarkModeTest", "")
                val viewHolder = holder as AnimeGridViewHolder
                viewHolder.render(charactersList[position], onPhotoClickListener, onItemSelected)
            }
        }
    }

    override fun getItemViewType(position: Int) = AnimeActivity.layoutDisplay

    override fun getItemCount() = charactersList.size

    fun updateList(list : List<AnimeCharacter>){
        charactersList = list
        notifyDataSetChanged()
    }
}
