package com.example.fixturemonkey.examples.stuffs

import com.example.fixturemonkey.examples.stuffs.dto.SomethingResponse
import com.example.fixturemonkey.examples.stuffs.repository.SomethingRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class SomethingService(
    private val somethingRepository: SomethingRepository,
) {
    fun something(now: LocalDate = LocalDate.now()): List<SomethingResponse> {
        val responses = somethingRepository.something()

        return responses.asSequence()
            .filter { it.isExpired().not() }
            .filter { it.isDisplay }
            .sortedBy { it.validEndDate }
            .toList()
    }
}
