package com.knthcame.myhealthkmp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val composeActivityScenario = createAndroidComposeRule<MainActivity>()

    @Test
    fun mainActivity_setsDefault_composable() {
        composeActivityScenario.onNodeWithTag("mainButton").assertIsDisplayed()
    }
}