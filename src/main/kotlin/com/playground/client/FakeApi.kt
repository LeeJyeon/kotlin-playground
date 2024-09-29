package com.playground.client

import com.playground.client.body.Response
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(name = "fakeApi", url = "\${application.feign-url.fakeApi}")
interface FakeApi {

    @GetMapping("/{type}", consumes = ["application/json"])
    fun getSample(@PathVariable type: String): Response
}