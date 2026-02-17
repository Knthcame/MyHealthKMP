package com.knthcame.myhealthkmp

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.knthcame.myhealthkmp.ui.home.Dashboard
import com.knthcame.myhealthkmp.ui.home.Diary
import com.knthcame.myhealthkmp.ui.home.Settings
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

    @Test
    fun homeScreen_displaysDiary_onBottomNavTabClick() {
        composeActivityScenario.onNodeWithTag(Diary.toString()).performClick()
        composeActivityScenario.onNodeWithTag(Diary.toString()).assertIsSelected()
        composeActivityScenario.onNodeWithTag("topBarTitle", useUnmergedTree = true)
            .assertTextEquals("Diary")
    }

    @Test
    fun homeScreen_displaysSettings_onBottomNavTabClick() {
        composeActivityScenario.onNodeWithTag(Settings.toString()).performClick()
        composeActivityScenario.onNodeWithTag(Settings.toString()).assertIsSelected()
        composeActivityScenario.onNodeWithTag("topBarTitle", useUnmergedTree = true)
            .assertTextEquals("Settings")
    }
}