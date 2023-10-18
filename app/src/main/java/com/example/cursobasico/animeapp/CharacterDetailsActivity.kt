package com.example.cursobasico.animeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.example.cursobasico.databinding.ActivityCharacterDetailsBinding
import com.example.cursobasico.animeapp.AnimeActivity.Companion.SEND_CHARACTER
import com.example.cursobasico.animeapp.AnimeActivity.Companion.UNKNOWN_CHARACTER
import com.squareup.picasso.Picasso

class CharacterDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCharacterDetailsBinding
    private var character: AnimeCharacter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharacterDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val index: Int? = intent.extras?.getInt(SEND_CHARACTER)
        character = index?.let { CharacterList.list.getOrNull(it) }

        character?.let {
            initUI()

        } ?: kotlin.run {
            val intentResult = Intent()
            intentResult.putExtra(UNKNOWN_CHARACTER, true)
            setResult(RESULT_CANCELED, intentResult)
            finish()
        }
    }

    private fun initUI() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.tvCharacterName.text = character!!.name
        binding.tvCharacterAnime.text = character!!.anime
        binding.tvCharacterMangaka.text = character!!.mangaka

        Picasso.get().load(character!!.photoURL).into(binding.imgCharacterPhotoDetails)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }

//    private fun Int.toDp() = TypedValue.applyDimension(
//        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), resources.displayMetrics
//    ).roundToInt()
}