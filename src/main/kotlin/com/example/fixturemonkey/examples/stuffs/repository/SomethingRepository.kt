package com.example.fixturemonkey.examples.stuffs.repository

import com.example.fixturemonkey.examples.stuffs.dto.SomethingResponse

fun interface SomethingRepository {
    fun something(): List<SomethingResponse>
}
