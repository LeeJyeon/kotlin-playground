package com.playground.client.config

import com.playground.client.FakeApi
import com.playground.client.body.FakeResponse
import org.springframework.stereotype.Component

@Component
class FakeFallBack: FakeApi {
    override fun getSample(type: String): FakeResponse {
        return FakeResponse("FakeResponse","FakeResponse")
    }
}