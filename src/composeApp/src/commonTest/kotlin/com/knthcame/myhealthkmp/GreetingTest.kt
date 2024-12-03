package com.knthcame.myhealthkmp

import kotlin.test.Test
import kotlin.test.assertContains

class GreetingTest {
    @Test
    fun greet_containsHello() {
        val result = Greeting().greet()

        assertContains(result, "Hello")
    }
}