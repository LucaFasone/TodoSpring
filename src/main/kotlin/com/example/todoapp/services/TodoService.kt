package com.example.todoapp.services

import com.example.todoapp.entity.TodoEntity
import com.example.todoapp.models.TodoModel
import com.example.todoapp.repository.TodoRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TodoService(private val todoRepository: TodoRepository) {
    
    fun getAllTodos(): List<TodoEntity> = todoRepository.findAll()
    
    fun getTodoById(id: UUID): TodoEntity? = todoRepository.findById(id).orElse(null)
    
    fun getTodosByCompleted(completed: Boolean): List<TodoEntity> = todoRepository.findByCompleted(completed)
    
    fun createTodo(todoEntity: TodoEntity): Result<TodoEntity> {
        if (todoEntity.title.isBlank()) {
            return Result.failure(IllegalArgumentException("Title cannot be blank"))
        }
        return Result.success(todoRepository.save(todoEntity))
    }
    
    fun updateTodo(todoModel: TodoModel): Result<TodoEntity> {
        val id = todoModel.id ?: return Result.failure(IllegalArgumentException("ID cannot be null"))
        val existingTodo = todoRepository.findById(id).orElse(null)
            ?: return Result.failure(IllegalArgumentException("Todo not found"))
        
        todoModel.title?.let { if (it.isNotBlank()) existingTodo.title = it }
        todoModel.completed?.let { existingTodo.completed = it }
        
        return Result.success(todoRepository.save(existingTodo))
    }
    
    fun deleteTodo(id: UUID): Boolean {
        return if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id)
            true
        } else {
            false
        }
    }
    
    fun entityToModel(entity: TodoEntity) = TodoModel(
        id = entity.id,
        title = entity.title,
        completed = entity.completed
    )
}