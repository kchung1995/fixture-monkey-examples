package com.example.fixturemonkey.examples.stuffs.repository

import com.example.fixturemonkey.examples.stuffs.dto.AnotherDataType
import com.example.fixturemonkey.examples.stuffs.dto.SomethingDataType
import com.example.fixturemonkey.examples.stuffs.dto.SomethingResponse
import com.example.fixturemonkey.examples.stuffs.repository.SomethingResponseStub.RESPONSES
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class SomethingStubClient() : SomethingRepository {
    override fun something(): List<SomethingResponse> = RESPONSES
}

object SomethingResponseStub {
    val RESPONSES =
        (0 until 3).map {
            SomethingResponse(
                name = it.toString(),
                phoneNumber = "010-1234-5678",
                validEndDate = LocalDate.of(2023, 12, it + 1),
                isDisplay = it % 2 == 1,
                notes = listOf("a", "b", "c"),
                reason = if (it % 2 == 1) "this is a stub note" else null,
                remindDate = if (it % 2 == 1) LocalDate.of(2023, 12, 10) else null,
                detail =
                    SomethingResponse.SomethingDetailResponse(
                        somethingDetailName = it.toString(),
                        comment = if (it % 2 == 0) "this is a stub comment" else null,
                        somethingDataType = SomethingDataType.entries[it],
                        anotherDataType = AnotherDataType.entries[it],
                    ),
            )
        }
}
