package com.example.todoapp.controllers

import com.example.todoapp.entity.TodoEntity
import com.example.todoapp.models.TodoModel
import com.example.todoapp.services.TodoService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.util.UUID

@RestController
class TodoController(private val todoService: TodoService) {
    
    @GetMapping("/status")
    fun status() = ResponseEntity.ok().body("OK")

    @GetMapping("/")
    fun getAllTodos(@RequestParam("id") id: String?): ResponseEntity<List<TodoEntity>> {
        return if (id == null) {
            ResponseEntity.ok(todoService.getAllTodos())
        } else {
            val uuid = try {
                UUID.fromString(id)
            } catch (e: IllegalArgumentException) {
                return ResponseEntity.badRequest().build()
            }
            val todo = todoService.getTodoById(uuid)
            ResponseEntity.ok(todo?.let { listOf(it) } ?: emptyList())
        }
    }

    @PostMapping("/")
    fun createTodo(@RequestBody todo: TodoEntity?): ResponseEntity<Any> {
        if (todo == null) {
            return ResponseEntity.badRequest().body(mapOf("message" to "Todo cannot be null!"))
        }
        
        return todoService.createTodo(todo)
            .fold(
                onSuccess = { savedTodo ->
                    ResponseEntity.created(URI.create("/${savedTodo.id}")).body(savedTodo)
                },
                onFailure = { exception ->
                    ResponseEntity.badRequest().body(mapOf("message" to exception.message))
                }
            )
    }

    @PatchMapping("/")
    fun updateTodo(@RequestBody todo: TodoModel?): ResponseEntity<Any> {
        if (todo == null) {
            return ResponseEntity.badRequest().body(mapOf("message" to "Todo cannot be null!"))
        }
        
        return todoService.updateTodo(todo)
            .fold(
                onSuccess = { updatedTodo ->
                    ResponseEntity.ok().body(updatedTodo)
                },
                onFailure = { exception ->
                    ResponseEntity.badRequest().body(mapOf("message" to exception.message))
                }
            )
    }

    @DeleteMapping("/")
    fun deleteTodo(@RequestParam("id") id: UUID?): ResponseEntity<Any> {
        if (id == null) {
            return ResponseEntity.badRequest().body(mapOf("message" to "ID cannot be null!"))
        }
        
        return if (todoService.deleteTodo(id)) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}