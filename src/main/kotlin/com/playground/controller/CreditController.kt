package com.playground.controller

import com.playground.service.CreditService
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.math.BigDecimal

@RequestMapping
@RestController
class CreditController(private val creditService: CreditService) {

    @PostMapping("/credit/{userId}")
    fun createNewCredit(@PathVariable userId: String) {
        creditService.createNewCredit(userId)
    }

    @PutMapping("/credit/{userId}")
    fun changeCredit(@PathVariable userId: String, @RequestBody amount: Int): BigDecimal {
        return creditService.changeAmountWith따닥방지(userId, amount)
    }
}