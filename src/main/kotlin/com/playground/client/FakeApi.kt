package com.playground.client

import com.playground.client.body.FakeResponse
import com.playground.client.config.CustomErrorDecoder
import com.playground.client.config.FakeFallBack
import com.playground.client.exception.ClientException
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import kotlin.jvm.Throws

@FeignClient(name = "fakeApi",
    url = "\${application.feign-url.fakeApi}",
    fallback = FakeFallBack::class,
    configuration =[CustomErrorDecoder::class] )
interface FakeApi {

    @Throws(ClientException::class)
    @GetMapping("/{type}")
    fun getSample(@PathVariable type: String): FakeResponse
}