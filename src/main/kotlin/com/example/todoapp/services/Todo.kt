package com.example.todoapp.services

import com.example.todoapp.entity.TodoEntity
import com.example.todoapp.models.TodoModel
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TodoService {
    fun entityToDTO(entity: TodoEntity) = TodoModel(
        id = entity.id ?: UUID.randomUUID(),
        title = entity.title,
        completed = entity.completed,
    )
}