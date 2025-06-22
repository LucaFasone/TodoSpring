package com.example.todoapp.repository

import com.example.todoapp.entity.TodoEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface TodoRepository : JpaRepository<TodoEntity, UUID> {
    fun findByTitle(title: String): TodoEntity?
    fun findByCompleted(completed: Boolean): TodoEntity?
}