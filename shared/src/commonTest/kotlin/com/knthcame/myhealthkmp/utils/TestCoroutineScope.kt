package com.knthcame.myhealthkmp.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher

private val testCoroutineScheduler: TestCoroutineScheduler = TestCoroutineScheduler()

@OptIn(ExperimentalCoroutinesApi::class)
val testDispatcher: CoroutineDispatcher =
    UnconfinedTestDispatcher(testCoroutineScheduler)

val testViewModelScope = CoroutineScope(testDispatcher + SupervisorJob())
