package com.example.cursobasico.todo

sealed class Categorias (var isSelected:Boolean = true) {

    object Personal : Categorias()
    object Business: Categorias()
    object Other: Categorias()
    object Escolar : Categorias()

}

