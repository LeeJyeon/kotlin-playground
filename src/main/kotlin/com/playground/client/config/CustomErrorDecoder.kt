package com.playground.client.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.playground.client.body.FakeResponse
import com.playground.client.exception.ClientException
import feign.Response
import feign.codec.ErrorDecoder
import lombok.extern.slf4j.Slf4j
import org.springframework.http.HttpStatus
import java.io.InputStreamReader
import java.lang.Exception

@Slf4j
class CustomErrorDecoder: ErrorDecoder {

    private val objectMapper = ObjectMapper()

    override fun decode(methodKey: String?, response: Response): Exception {

        val res = objectMapper.readValue(
            InputStreamReader(response.body().asInputStream()),
            FakeResponse::class.java)

        throw ClientException(res.code +":"+ res.message, HttpStatus.valueOf(response.status()))
    }
}