package com.example.fixturemonkey.examples.stuffs

import com.example.fixturemonkey.examples.stuffs.dto.SomethingResponse
import com.example.fixturemonkey.examples.stuffs.repository.SomethingRepository
import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import com.navercorp.fixturemonkey.kotlin.setExp
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import java.time.LocalDate

@ExtendWith(MockKExtension::class)
internal class SomethingServiceMockTest {
    @InjectMockKs
    private lateinit var somethingService: SomethingService

    @MockK
    private lateinit var somethingRepository: SomethingRepository

    private val fixtureMonkey =
        FixtureMonkey.builder()
            .plugin(KotlinPlugin())
            .build()

    @DisplayName("isDisplay가 아닌 response는 filter된다")
    @Test
    fun `responses without isDisplay true are filtered`() {
        // given
        val now = LocalDate.of(2023, 12, 10)
        val responses =
            (1..100).map {
                fixtureMonkey.giveMeBuilder<SomethingResponse>()
                    .setExp(SomethingResponse::validEndDate, LocalDate.of(2023, 12, 11))
                    .sample()
            }

        every { somethingRepository.something() } returns responses

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
        val expiredDate = LocalDate.of(2023, 12, 9)
        val nonExpiredDate = LocalDate.of(2023, 12, 11)
        val responses =
            (1..100).map {
                fixtureMonkey.giveMeBuilder<SomethingResponse>()
                    .setExp(SomethingResponse::isDisplay, true)
                    .setExp(SomethingResponse::validEndDate, if (it % 2 == 0) expiredDate else nonExpiredDate)
                    .sample()
            }

        every { somethingRepository.something() } returns responses

        // when
        val result = somethingService.something(now)

        // then
        assertThat(result.none { it.isExpired(now) }).isTrue
        assertThat(result.all { it.validEndDate == nonExpiredDate }).isTrue
    }

    @DisplayName("response가 validEndDate 기준으로 오름차순 정렬된다")
    @Test
    fun `responses are sorted ascending by validEndDate`() {
        // given
        val now = LocalDate.of(2023, 12, 10)
        val validEndDates = listOf("2023-12-31", "2023-12-30", "2023-12-29", "2023-12-11")

        val responses =
            (0 until 4).map {
                fixtureMonkey.giveMeBuilder<SomethingResponse>()
                    .setExp(SomethingResponse::isDisplay, true)
                    .setExp(SomethingResponse::validEndDate, LocalDate.parse(validEndDates[it]))
                    .sample()
            }

        every { somethingRepository.something() } returns responses

        // when
        val result = somethingService.something(now)

        // then
        assertThat(result).isSortedAccordingTo { o1, o2 -> o1.validEndDate.compareTo(o2.validEndDate) }
    }
}
