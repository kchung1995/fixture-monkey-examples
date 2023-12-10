package com.example.fixturemonkey.examples.stuffs

import com.example.fixturemonkey.examples.stuffs.dto.SomethingResponse
import com.example.fixturemonkey.examples.stuffs.repository.SomethingRepository
import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class SomethingServiceInterfaceTest {
    private val somethingService = SomethingService(SomethingImplementationClient())

    @DisplayName("isDisplay가 아닌 response는 filter된다")
    @Test
    fun `responses without isDisplay true are filtered`() {
        // given
        val now = LocalDate.of(2023, 12, 10)

        // when
        val result = somethingService.something(now)

        // then
        assertThat(result.all { it.isDisplay }).isTrue
    }

    @DisplayName("isExpired인 response는 filter된다")
    @Test
    fun `responses with isExpired true are filtered`() {
        // given
        val now = LocalDate.of(2023, 12, 10)

        // when
        val result = somethingService.something(now)

        // then
        assertThat(result.none { it.isExpired(now) }).isTrue
    }

    @DisplayName("response가 validEndDate 기준으로 오름차순 정렬된다")
    @Test
    fun `responses are sorted ascending by validEndDate`() {
        // given
        val now = LocalDate.of(2023, 12, 10)

        // when
        val result = somethingService.something(now)

        // then
        assertThat(result).isSortedAccordingTo { o1, o2 -> o1.validEndDate.compareTo(o2.validEndDate) }
    }
}

private class SomethingImplementationClient() : SomethingRepository {
    private val fixtureMonkey =
        FixtureMonkey.builder()
            .plugin(KotlinPlugin())
            .build()

    override fun something(): List<SomethingResponse> {
        return (1..100).map {
            fixtureMonkey.giveMeBuilder<SomethingResponse>()
                .sample()
        }
    }
}
