package com.example.fixturemonkey.examples.stuffs.dto

import java.time.LocalDate

data class SomethingResponse(
    val name: String,
    val phoneNumber: String,
    val validEndDate: LocalDate,
    val isDisplay: Boolean,
    val notes: List<String>,
    val reason: String?,
    val remindDate: LocalDate?,
    val detail: SomethingDetailResponse,
) {
    data class SomethingDetailResponse(
        val somethingDetailName: String,
        val comment: String?,
        val somethingDataType: SomethingDataType,
        val anotherDataType: AnotherDataType,
    )

    fun isExpired(now: LocalDate = LocalDate.now()) = validEndDate <= now
}

enum class SomethingDataType(val code: String, val description: String) {
    INITIAL("01", "초기형"),
    PLURAL("02", "복수형"),
    BROTHER("03", "우리형"),
    CIRCULAR("04", "환형"),
}

enum class AnotherDataType(val description: String) {
    SEOUL("서울"),
    TOKYO("도쿄"),
    SYDNEY("시드니"),
}
