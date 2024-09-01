package com.playground.service

import com.playground.entity.Credit
import com.playground.repository.CreditRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors


@SpringBootTest
//@Transactional // 멀티스레드 + new tran 이라 트랜잭션 테스트는 불가능함
class CreditServiceTest {

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
    fun 천천히라도_처리해서_50원을_모두입금하는_테스트() {
        val numberOfThreads = 50;
        val executorService = Executors.newFixedThreadPool(numberOfThreads)
        val latch = CountDownLatch(numberOfThreads)

        for (i in 1..numberOfThreads) {
            executorService.submit {
                try {
                    creditService.changeAmountWith천천히라도_처리(testUser, 1)
                } finally {
                    latch.countDown()
                }
            }
        }
        latch.await()

        val findById = creditRepository.findById(testUser)
        println(findById.get().toString())
        Assertions.assertThat(findById.get().point.compareTo(BigDecimal.valueOf(50))).isEqualTo(0)
    }

    @Test
    fun 따닥을방지해서_딱_한건만_처리하는_테스트() {
        val numberOfThreads = 50;
        val executorService = Executors.newFixedThreadPool(numberOfThreads)
        val latch = CountDownLatch(numberOfThreads)

        for (i in 1..numberOfThreads) {
            executorService.submit {
                try {
                    creditService.changeAmountWith따닥방지(testUser, 1)
                } finally {
                    latch.countDown()
                }
            }
        }
        latch.await()

        val findById = creditRepository.findById(testUser)
        println(findById.get().toString())
        Assertions.assertThat(findById.get().point.compareTo(BigDecimal.ONE)).isEqualTo(0)
    }

}