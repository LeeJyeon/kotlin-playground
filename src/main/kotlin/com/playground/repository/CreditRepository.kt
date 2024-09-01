package com.playground.repository

import com.playground.entity.Credit
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CreditRepository : CrudRepository<Credit, String>{

}