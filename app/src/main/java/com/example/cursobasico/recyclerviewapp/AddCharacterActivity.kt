package com.example.cursobasico.recyclerviewapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.cursobasico.R
import com.example.cursobasico.databinding.ActivityAddCharacterBinding
import java.util.*

class AddCharacterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddCharacterBinding
    private lateinit var character:AnimeCharacter
    private var index: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCharacterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        index = intent.extras?.getInt(AnimeActivity.SEND_CHARACTER)

        initUI()
        initListeners()
    }

    private fun initUI() {
        setSupportActionBar(binding.toolbarAddCharacter)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.anime_title)
        supportActionBar?.subtitle = getString(
            if (index == null) R.string.add_character_subtitle
            else R.string.edit_character_subtitle
        )

        binding.tvAddCharacterTitle.text = getString(
            if (index == null) R.string.add_character_title
            else R.string.edit_character_title
        )
        val characterToEdit = index?.let { CharacterList.list.getOrNull(it) }

        binding.etAddName.setText(characterToEdit?.name.orEmpty())
        binding.etAddAnime.setText(characterToEdit?.anime.orEmpty())
        binding.etAddMangaka.setText(characterToEdit?.mangaka.orEmpty())
        binding.etAddPhoto.setText(characterToEdit?.photoURL.orEmpty())
    }

    private fun initListeners() {
        binding.btnSaveCharacter.setOnClickListener {
            if (validateFields()) confirmAction()
        }
    }

    private fun confirmAction() {
        val isCharecterEdited: Boolean = index != null
        Log.i("ñññ", "Name: ${character.name}, Anime: ${character.anime}, Autor: ${character.mangaka}, Photo: ${character.photoURL}")

        val msj: String = getString(
            if (isCharecterEdited) R.string.confirm_edit_character
            else R.string.confirm_save_character
        )

        val builder = AlertDialog.Builder(this)
        builder.setMessage(msj)

        builder.setPositiveButton(getString(R.string.acept)) { _, _ ->

            if (isCharecterEdited) {
                editCharacter()
            } else {
                addCharacter()
            }
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun addCharacter() {
        CharacterList.list.add(character)
        Toast.makeText(this, getString(R.string.character_added), Toast.LENGTH_SHORT).show()

        val extra = Intent().putExtra(AnimeActivity.EDITED_CHARACTER, false)
        setResult(RESULT_OK, extra)
        finish()
    }

    private fun editCharacter() {
        index?.let {
            CharacterList.list[it] = character
            Toast.makeText(this, getString(R.string.character_edited), Toast.LENGTH_SHORT).show()

            val extra = Intent().putExtra(AnimeActivity.EDITED_CHARACTER, true)
            setResult(RESULT_OK, extra)
            finish()

        } ?: kotlin.run {
            val extra = Intent().putExtra(AnimeActivity.EDITED_CHARACTER, null as Boolean?)
            setResult(RESULT_CANCELED, extra)
            finish()
        }
    }

    private fun validateFields(): Boolean {
        val name: String = binding.etAddName.text.toString().trim()
        val anime: String = binding.etAddAnime.text.toString().trim()
        val mangaka: String = binding.etAddMangaka.text.toString().trim()
        val photo: String = binding.etAddPhoto.text.toString().trim()

        val isValidName: Boolean = name.isNotEmpty() && name.length < 50
        val isValidAnime: Boolean = anime.isNotEmpty() && anime.length < 50
        val isValidMangaka: Boolean = mangaka.isNotEmpty() && mangaka.length < 50

        val areFieldsValid: Boolean =
            isValidName && isValidAnime && isValidMangaka && photo.isNotEmpty()

        if (!areFieldsValid) {
            val msj = getString(
                if (!isValidName) {
                    R.string.error_invalid_name

                } else if (!isValidAnime) {
                    R.string.error_invalid_anime

                } else if (!isValidMangaka) {
                    R.string.error_invalid_anime

                } else {
                    R.string.error_invalid_photo
                }
            )
            Toast.makeText(this, msj, Toast.LENGTH_SHORT).show()

        } else {
            character = AnimeCharacter(
                name = name.titleCase(),
                anime = anime.titleCase(),
                mangaka = mangaka.titleCase(),
                photoURL = photo
            )
        }

        return areFieldsValid
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        if(index != null){
            val builder = AlertDialog.Builder(this)
            builder.setMessage(getString(R.string.confirm_back_character))

            builder.setPositiveButton(getString(R.string.acept)) { _, _ ->
                super.onBackPressed()
            }
            builder.setNegativeButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()

        } else {
            super.onBackPressed()
        }
    }

    private fun String.titleCase():String{
        return this.split(" ").joinToString(" "){ str ->
            val first: Char = Character.toUpperCase(str[0])
            val rest:String = str.substring(1).lowercase()
            first + rest
        }
    }
}