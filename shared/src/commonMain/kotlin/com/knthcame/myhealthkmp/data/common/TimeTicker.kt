package com.knthcame.myhealthkmp.data.common

import kotlin.time.Duration
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

/** Creates a flow that repeatedly emits every time the specified [interval] elapses.*/
fun timeTicker(interval: Duration) = flow {
    while (true) {
        emit(Unit)
        delay(interval)
    }
}
