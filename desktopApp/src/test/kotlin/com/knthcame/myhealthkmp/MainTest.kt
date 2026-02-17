package com.knthcame.myhealthkmp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.knthcame.myhealthkmp.koin.appModule
import com.knthcame.myhealthkmp.ui.home.Dashboard
import org.junit.Rule
import org.junit.Test

class MainTest {
    @get:Rule
    val rule = createComposeRule()

    @Test
    fun main_showsDashboardScreen_onLaunch() {
        rule.setContent {
            initKoin { appModule }

            MyHealthWindow { }
        }

        rule.onNodeWithTag(Dashboard.toString()).assertIsSelected()
        rule.onNodeWithTag("dashboardTopBarTitle", true).assertIsDisplayed()
    }
}
