package com.example.todoapp.services

import com.example.todoapp.entity.TodoEntity
import com.example.todoapp.models.TodoModel
import com.example.todoapp.repository.TodoRepository
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TodoServiceTest {

    @Test
    fun `should create todo successfully with valid title`() {
        // Given
        val todoRepository = mock(TodoRepository::class.java)
        val todoService = TodoService(todoRepository)
        val todoEntity = TodoEntity(title = "Test Todo")
        val savedTodo = TodoEntity(id = UUID.randomUUID(), title = "Test Todo")
        
        `when`(todoRepository.save(todoEntity)).thenReturn(savedTodo)
        
        // When
        val result = todoService.createTodo(todoEntity)
        
        // Then
        assertTrue(result.isSuccess)
        assertEquals(savedTodo, result.getOrNull())
    }

    @Test
    fun `should fail to create todo with blank title`() {
        // Given
        val todoRepository = mock(TodoRepository::class.java)
        val todoService = TodoService(todoRepository)
        val todoEntity = TodoEntity(title = "  ")
        
        // When
        val result = todoService.createTodo(todoEntity)
        
        // Then
        assertTrue(result.isFailure)
        assertEquals("Title cannot be blank", result.exceptionOrNull()?.message)
    }

    @Test
    fun `should convert entity to model correctly`() {
        // Given
        val todoRepository = mock(TodoRepository::class.java)
        val todoService = TodoService(todoRepository)
        val id = UUID.randomUUID()
        val entity = TodoEntity(id = id, title = "Test Todo", completed = true)
        
        // When
        val model = todoService.entityToModel(entity)
        
        // Then
        assertEquals(id, model.id)
        assertEquals("Test Todo", model.title)
        assertEquals(true, model.completed)
    }
}