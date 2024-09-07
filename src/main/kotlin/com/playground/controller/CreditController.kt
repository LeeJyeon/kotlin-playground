package com.playground.controller

import com.playground.repository.document.ReceiptRepository
import com.playground.service.CreditService
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

@RequestMapping
@RestController
class CreditController(private val creditService: CreditService, private val receiptRepository: ReceiptRepository) {


    @PostMapping("/credit/{userId}")
    fun createNewCredit(@PathVariable userId: String) {
        creditService.createNewCredit(userId)
    }

    @PutMapping("/credit/{userId}")
    fun changeCredit(@PathVariable userId: String, @RequestBody amount: Int): BigDecimal {
        return creditService.changeAmountWith따닥방지(userId, amount)
    }
}