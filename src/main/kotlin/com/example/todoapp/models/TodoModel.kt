package com.example.todoapp.models

import java.util.UUID


data class TodoModel(
    val id: UUID?,
    val title: String?,
    val completed: Boolean?,
)