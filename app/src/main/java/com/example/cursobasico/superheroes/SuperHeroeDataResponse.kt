package com.example.cursobasico.superheroes

import com.google.gson.annotations.SerializedName

data class SuperHeroeDataResponse(
    @SerializedName("response") val respuesta: String,
    @SerializedName("results") val superheroes: List<SuperHeroeItemResponse>
)

data class SuperHeroeItemResponse(
    @SerializedName("id") val heroe_id: String,
    @SerializedName("name") val heroe_name: String,
    @SerializedName("image") val heroe_img : SuperHeroeImageResponse
)

data class SuperHeroeImageResponse(
    @SerializedName("url") val img_url: String
)