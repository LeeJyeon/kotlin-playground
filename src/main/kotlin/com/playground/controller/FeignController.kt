package com.playground.controller

import com.playground.client.FakeApi
import com.playground.client.body.Response
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class FeignController(private val fakeApi: FakeApi) {

    @GetMapping("/test-feign/{type}")
    fun changeCredit(@PathVariable type: String) :Response{
        return fakeApi.getSample(type)
    }
}