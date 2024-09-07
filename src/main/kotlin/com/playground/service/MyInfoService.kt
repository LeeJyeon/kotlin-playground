package com.playground.service

import com.playground.repository.jpa.CreditRepository
import com.playground.repository.document.ReceiptRepository
import com.playground.service.dto.MyInfo
import com.playground.service.dto.Used
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MyInfoService(private val creditRepository: CreditRepository,
                    private val receiptRepository: ReceiptRepository) {

    @Transactional(readOnly = true)
    fun viewMyInfo(userId: String) :MyInfo {

        val credit = (creditRepository.findByIdOrNull(userId)
                ?: throw NoSuchElementException("No Credit Info"))

        val receipts = receiptRepository.findByUserId(userId)


       val usedInfos =  receipts.map {
            receipt->
            Used(
                    createdAt = receipt.createdAt,
                    context = receipt.context
            )
        }

        return MyInfo(
                userId = userId,
                point = credit.point,
                usedInfos = usedInfos
        )
    }
}