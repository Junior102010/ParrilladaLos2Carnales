package com.edu.ucne.parrilladalos2carnales

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.edu.ucne.parrilladalos2carnales.presentacion.componentes.AppTopBar
import com.edu.ucne.parrilladalos2carnales.ui.theme.ParrilladaLos2CarnalesTheme
import org.junit.Rule
import org.junit.Test

class AppTopBarTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun appTopBar_displaysTitle() {
        composeTestRule.setContent {
            ParrilladaLos2CarnalesTheme {
                AppTopBar(title = "Test Title")
            }
        }

        composeTestRule.onNodeWithText("Test Title").assertIsDisplayed()
    }
}
