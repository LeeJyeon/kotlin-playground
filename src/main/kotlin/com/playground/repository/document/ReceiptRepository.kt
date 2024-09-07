package com.playground.repository.document

import com.playground.entity.Receipt
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ReceiptRepository: MongoRepository<Receipt, String> {
    fun findByUserId(userId: String): List<Receipt>
}