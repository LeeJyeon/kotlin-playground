package com.playground.repository

import com.playground.entity.Credit
import jakarta.persistence.LockModeType
import jakarta.persistence.QueryHint
import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.QueryHints
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CreditRepository : CrudRepository<Credit, String>{


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @QueryHints(QueryHint(name = "javax.persistence.lock.timeout", value = "0"))
    @Query("SELECT c FROM Credit c WHERE c.userId = :id")
    fun findByIdWithLock(id: String): Optional<Credit>

}