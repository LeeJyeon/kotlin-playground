package com.playground.entity

import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "receipt")
data class Receipt (
        @Id
        var id: String? = null,
        var userId: String,
        var createdAt: Date,
        var context: String
)