package com.example.todoapp.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "todos")
data class TodoEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "VARCHAR(36)")
    val id: UUID? = null,

    @Column(nullable = false)
    var title: String = "",

    @Column(nullable = false)
    var completed: Boolean = false
    )