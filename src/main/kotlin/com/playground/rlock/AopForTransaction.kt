package com.playground.rlock

import org.aspectj.lang.ProceedingJoinPoint
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class AopForTransaction {

//    @Transactional(propagation = Propagation.REQUIRES_NEW)
    // 따닥 사용용도로는 Tx 묶을 필요가 없음
    @Throws(Throwable::class)
    fun proceed(joinPoint: ProceedingJoinPoint): Any? {
        return joinPoint.proceed()
    }

}