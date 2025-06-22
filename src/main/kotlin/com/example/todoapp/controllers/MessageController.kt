package com.example.todoapp.controllers

import com.example.todoapp.entity.TodoEntity
import com.example.todoapp.models.TodoModel
import com.example.todoapp.repository.TodoRepository
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
class MessageController(private val db: TodoRepository) {
    @GetMapping("/status")
    fun status() = ResponseEntity.ok().body("OK")

    @GetMapping("/")
    fun index(@RequestParam("id") id: String?)
        = if(id == null) db.findAll() else db.findById(UUID.fromString(id))
            .map { listOf(it) }.orElse(emptyList())


    @PostMapping("/")
    fun create(@RequestBody todo: TodoEntity?): ResponseEntity<Any>{
        if(todo == null){
            val error = mapOf("message" to "Todo cannot be null!")
            return ResponseEntity.badRequest().body(error)
        }
        val (_,title) = todo
        if(title.isEmpty()){
            return ResponseEntity.badRequest().body(mapOf("message" to "title cannot be null!"))
        }
        val todoSaved = db.save<TodoEntity>(todo)
        return ResponseEntity.created(URI.create("/${todoSaved.id}")).body(todoSaved)
    }

    @PatchMapping("/")
    fun patch(@RequestBody todo: TodoModel?): ResponseEntity<Any>{
        if(todo == null){
            return ResponseEntity.badRequest().body(mapOf("message" to "Todo cannot be null!"))
        }
        val (id,title,completed) = todo
        val dbTodo = id?.let { db.findById(it).orElse(null) }
            ?: return ResponseEntity.notFound().build()
        title?.let { dbTodo.title = it }
        completed?.let { dbTodo.completed = it }
        val updated = db.save<TodoEntity>(dbTodo)
        return ResponseEntity.ok().body(updated)
    }

    @DeleteMapping("/")
    fun delete(@RequestParam("id") id: UUID?): ResponseEntity<Any>{
        val dbTodo = id?.let { db.deleteById(it) }
        ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(dbTodo)
    }
}