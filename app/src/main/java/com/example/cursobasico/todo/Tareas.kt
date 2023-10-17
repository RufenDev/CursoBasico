package com.example.cursobasico.todo

data class Tareas (val name:String, val categoria:Categorias, var isSelected:Boolean = false)