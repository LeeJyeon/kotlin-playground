package com.playground.service

import com.playground.entity.Credit
import com.playground.repository.jpa.CreditRepository
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.RepeatedTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors


@SpringBootTest
//@Transactional // 멀티스레드 라 트랜잭션 테스트는 불가능함
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
    @Disabled // 분산락에서 Tx 를 안잡기 떄문에 처리할 수 없다
    fun 천천히라도_처리해서_100원을_모두입금하는_테스트() {
        val numberOfThreads = 100;
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
        assertThat(findById.get().point.compareTo(BigDecimal.valueOf(100))).isEqualTo(0)
    }

    @RepeatedTest(10, failureThreshold = 2)
    fun 따닥을방지해서_다섯건_미만_처리하는_테스트() {
        val numberOfThreads = 100;
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

        val point = findById.get().point
        assertThat(point.toInt()).isLessThanOrEqualTo(5)
        Thread.sleep(100)
    }

    @Test
    fun 따닥을방지해서_딱_한건만_처리하는_테스트withDbLock() {
        val numberOfThreads = 100;
        val executorService = Executors.newFixedThreadPool(numberOfThreads)
        val latch = CountDownLatch(numberOfThreads)

        for (i in 1..numberOfThreads) {
            executorService.submit {
                try {
                    creditService.changeAmountWithDbLock(testUser, 1)
                } finally {
                    latch.countDown()
                }
            }
        }
        latch.await()

        val findById = creditRepository.findById(testUser)
        println(findById.get().toString())
        assertThat(findById.get().point.compareTo(BigDecimal.ONE)).isEqualTo(0)
    }

}