package com.playground.rlock

import lombok.extern.slf4j.Slf4j
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.reflect.MethodSignature
import org.redisson.api.RedissonClient
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Aspect
@Component
@Slf4j
class DistributedLockAop(
        private val redissonClient: RedissonClient,
        private val aopForTransaction: AopForTransaction
) {
    companion object{
        private const val REDISSON_LOCK_PREFIX = "LOCK:"
    }

    private val log = LoggerFactory.getLogger(DistributedLockAop::class.java)


    @Around("@annotation(com.playground.rlock.DistributedLock)")
    @Throws(Throwable::class)
    fun lock(joinPoint: ProceedingJoinPoint): Any? {
        val signature = joinPoint.signature as MethodSignature
        val method: Method = signature.method
        val distributedLock: DistributedLock = method.getAnnotation(DistributedLock::class.java)

        val key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(
                signature.parameterNames,
                joinPoint.args,
                distributedLock.key)

        val rLock = redissonClient.getLock(key)

        return try {
            val available = rLock.tryLock(
                    distributedLock.waitTime,
                    distributedLock.leaseTime,
                    distributedLock.timeUnit
            )
            if (!available) {
                log.info("Getting Lock is fail {} {} {}",
                        distributedLock.waitTime,
                        distributedLock.leaseTime,
                        distributedLock.timeUnit)
                return false
            }

            aopForTransaction.proceed(joinPoint)
        } catch (e: InterruptedException) {
            throw e
        }finally {
            try {
                rLock.unlock()
            } catch (e: Exception) {
                log.info("Redisson Lock Already UnLock")
            }
        }
    }
}