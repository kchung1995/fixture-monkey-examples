package com.example.fixturemonkey.examples.stuffs

import com.example.fixturemonkey.examples.stuffs.dto.SomethingResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class SomethingController(
    private val somethingService: SomethingService,
) {
    @GetMapping("/something")
    fun something(): List<SomethingResponse> = somethingService.something()
}
