package com.playground.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import lombok.Getter
import lombok.ToString
import java.math.BigDecimal

@Entity
data class Credit(
        @Id var userId: String,
        var point: BigDecimal
){
    fun updateAmount(amount: Int) {
        point = point.add(amount.toBigDecimal())
        if(point < BigDecimal.ZERO){
            throw IllegalStateException("잔액이 0보다 작을 수는 없습니다.")
        }
    }
}