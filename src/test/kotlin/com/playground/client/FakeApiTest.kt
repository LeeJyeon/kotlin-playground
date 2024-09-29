package com.playground.client

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.playground.client.exception.ClientException
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.util.ResourceUtils
import java.nio.file.Files

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
class FakeApiTest {

    @Autowired
    private lateinit var fakeApi: FakeApi // Feign Client

    @Test
    @DisplayName("정상테스트")
    fun test01() {
        // given
        val file =
            ResourceUtils.getFile("src/test/resources/__files/payload/normal.json").toPath()

        stubFor(
            get(urlPathEqualTo("/normal"))
                .willReturn(
                    aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(Files.readAllBytes(file))
                )
        )

        // when
        val response = fakeApi.getSample("normal")

        //then
        assertThat(response.code).isEqualTo("success")
        assertThat(response.message).isEqualTo("This is success")
    }

    @Test
    @DisplayName("400에러 발생 테스트")
    fun test02() {
        // given
        val file =
            ResourceUtils.getFile("src/test/resources/__files/payload/error400.json").toPath()

        stubFor(
            get(urlPathEqualTo("/400-exception"))
                .willReturn(
                    aResponse()
                        .withStatus(HttpStatus.BAD_REQUEST.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(Files.readAllBytes(file))
                )
        )

        // when
        try {
            fakeApi.getSample("400-exception")
            fail("예외 발생해야 함")
        } catch (ex:ClientException) {
            assertThat(ex.responseStatus).isEqualTo(HttpStatus.BAD_REQUEST)
            assertThat(ex.message).isEqualTo("exception:throw 400 status")
        }
    }

    @Test
    @DisplayName("500에러 발생 테스트")
    fun test03() {
        // given
        val file =
            ResourceUtils.getFile("src/test/resources/__files/payload/error500.json").toPath()

        stubFor(
            get(urlPathEqualTo("/500-exception"))
                .willReturn(
                    aResponse()
                        .withStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withHeader("Content-Type", "application/json")
                        .withBody(Files.readAllBytes(file))
                )
        )

        // when
        try {
            fakeApi.getSample("500-exception")
            fail("예외 발생해야 함")
        } catch (ex:ClientException) {
            assertThat(ex.responseStatus).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
            assertThat(ex.message).isEqualTo("exception:throw 500 status")
        }
    }


}