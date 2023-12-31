package com.example.cursobasico.animeapp

import android.graphics.Bitmap

data class AnimeCharacter(
    val name: String,
    val anime: String,
    val mangaka: String,
    val photoURL: String,
    var localPhoto: Bitmap? = null,
    var isSelected: Boolean = false
)