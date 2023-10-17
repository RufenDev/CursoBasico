package com.example.cursobasico.superheroes

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("/api/6924539124224400/search/{name}")
    suspend fun getSuperHeroes(@Path("name") superHeroeName:String):Response<SuperHeroeDataResponse>

    @GET("/api/6924539124224400/{id}")
    suspend fun getSuperHeroeDetail(@Path("id") superHeroeID:String):Response<SuperHeroeDetailsResponse>
}