package com.playground.service.dto

import java.math.BigDecimal

data class MyInfo(
        val userId: String,
        val point: BigDecimal,
        val usedInfos: List<Used>
)