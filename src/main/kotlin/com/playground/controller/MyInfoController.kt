package com.playground.controller

import com.playground.service.MyInfoService
import com.playground.service.dto.MyInfo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class MyInfoController(private val myInfoService: MyInfoService) {

    @GetMapping("/my/{userId}")
    fun getSimpleInfo(@PathVariable userId: String): MyInfo {
        return myInfoService.viewMyInfo(userId)
    }
}