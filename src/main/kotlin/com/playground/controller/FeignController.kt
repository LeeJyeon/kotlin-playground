package com.playground.controller

import com.playground.client.FakeApi
import com.playground.client.body.FakeResponse
import com.playground.client.exception.ClientException
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class FeignController(private val fakeApi: FakeApi) {

    private val log = LoggerFactory.getLogger(FeignController::class.java)

    @GetMapping("/test-feign/{type}")
    fun changeCredit(@PathVariable type: String) :FakeResponse{
        try {
            return fakeApi.getSample(type)
        } catch (e: ClientException) {
            log.error("exception occur", e)
            return FakeResponse()
        }
    }
}