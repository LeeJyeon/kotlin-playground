package com.playground.service

import com.playground.entity.Credit
import com.playground.repository.jpa.CreditRepository
import com.playground.utility.rlock.DistributedLock
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class CreditService(private val creditRepository: CreditRepository) {

    private val log = LoggerFactory.getLogger(CreditService::class.java)

    @Transactional
    fun createNewCredit(userId: String) {
        val findById = creditRepository.findById(userId)
        if (findById.isPresent) {
            throw IllegalStateException("이미 등록된 정보가 있습니다.")
        }
        creditRepository.save(Credit(userId = userId, point = BigDecimal.ZERO))
    }

    @Transactional
    @DistributedLock(key = "'changeAmountAll'.concat('-').concat(#userId)", waitTime = 10, leaseTime = 3)
    fun changeAmountWith천천히라도_처리(userId: String, amount: Int): BigDecimal {
        val findById = creditRepository.findById(userId)
        if (findById.isEmpty) {
            log.error("미존재사용자입니다. [{}]",userId)
            throw IllegalStateException("존재하지 않는 사용자입니다.")
        }
        val credit = findById.get()
        credit.updateAmount(amount)
        creditRepository.save(credit)
        log.info("저장한 포인트 : " + credit.toString())
        return credit.point // 이게 커밋 찍히고 해제한다.
    }

    @Transactional
    @DistributedLock(key = "'changeAmountJustOne'.concat('-').concat(#userId)", waitTime = -1, leaseTime = 3)
    fun changeAmountWith따닥방지(userId: String, amount: Int): BigDecimal {
        val findById = creditRepository.findById(userId)
        if (findById.isEmpty) {
            log.error("미존재사용자입니다. [{}]",userId)
            throw IllegalStateException("존재하지 않는 사용자입니다.")
        }
        val credit = findById.get()
        credit.updateAmount(amount)
        creditRepository.save(credit)
        log.info("저장한 포인트 : " + credit.toString())
        return credit.point // 이게 커밋 찍히고 해제한다.
    }

    @Transactional
    fun changeAmountWithDbLock(userId: String, amount: Int): BigDecimal {
        val findById = creditRepository.findByIdWithLock(userId)
        if (findById.isEmpty) {
            log.error("미존재사용자입니다. [{}]",userId)
            throw IllegalStateException("존재하지 않는 사용자입니다.")
        }
        val credit = findById.get()
        credit.updateAmount(amount)
        creditRepository.save(credit)
        log.info("저장한 포인트 : " + credit.toString())
        return credit.point // 이게 커밋 찍히고 해제한다.
    }
}