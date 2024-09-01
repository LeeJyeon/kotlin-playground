package com.playground.service

import com.playground.entity.Credit
import com.playground.repository.CreditRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors


@SpringBootTest
@Transactional
class CreditServiceSimpleTest {

    @Autowired
    private lateinit var creditService: CreditService
    @Autowired
    private lateinit var creditRepository: CreditRepository
    val testUser = "sanzio"

    @BeforeEach
    fun setUp() {
        creditRepository.save(Credit(userId = testUser, point = BigDecimal.ZERO))
        val findById = creditRepository.findById(testUser)
        println(findById.get().toString())
    }

    @Test
    fun 단순기능점검() {
        creditService.changeAmountWith천천히라도_처리(testUser, 1)
        val findById = creditRepository.findById(testUser)
        println(findById.get().toString())
        assertThat(findById.get().point.compareTo(BigDecimal.valueOf(1))).isEqualTo(0)
    }
}