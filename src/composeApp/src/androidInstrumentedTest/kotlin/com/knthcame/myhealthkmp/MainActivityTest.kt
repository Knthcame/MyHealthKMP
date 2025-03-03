package com.knthcame.myhealthkmp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.knthcame.myhealthkmp.ui.home.Dashboard
import org.junit.Rule
import org.junit.Test

class MainActivityTest {

    @get:Rule
    val composeActivityScenario = createAndroidComposeRule<MainActivity>()

    @Test
    fun mainActivity_showsDashboardScreen_onStart() {
        composeActivityScenario.onNodeWithTag(Dashboard.toString()).assertIsSelected()
        composeActivityScenario.onNodeWithTag("dashboardTopBarTitle", true).assertIsDisplayed()
    }
}