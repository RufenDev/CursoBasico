package com.example.cursobasico.superheroes

import com.google.gson.annotations.SerializedName

data class SuperHeroeDetailsResponse(
    @SerializedName("name") val name: String,
    @SerializedName("powerstats") val powerStats: SuperHeroePowerStats,
    @SerializedName("image") val image: SuperHeroeImage,
    @SerializedName("biography") val biography: SuperHeroeBiography,
    @SerializedName("appearance") val appearance: SuperHeroeAppearance
)

data class SuperHeroePowerStats(
    @SerializedName("intelligence") val intelligence: String,
    @SerializedName("strength") val strength: String,
    @SerializedName("speed") val speed: String,
    @SerializedName("durability") val durability: String,
    @SerializedName("power") val power: String,
    @SerializedName("combat") val combat: String,
)

data class SuperHeroeImage(
    @SerializedName("url") val url: String
)

data class SuperHeroeBiography(
    @SerializedName("full-name") val fullName: String,
    @SerializedName("publisher") val publisher: String
)

data class SuperHeroeAppearance(
    @SerializedName("gender") val gender: String,
    @SerializedName("race") val race: String,
    @SerializedName("height") val height:List<String>,
    @SerializedName("weight") val weight:List<String>
)