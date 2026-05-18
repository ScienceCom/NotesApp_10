package com.example.notesapp

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.notesapp.data.NotesRepository
import com.example.notesapp.data.NotesViewModel
import io.mockk.mockk
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

class NotesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockRepository = mockk<NotesRepository>(relaxed = true)

    @Before
    fun setup() {
        stopKoin() // Cegah bentrok kalau Koin sudah nyala
        startKoin {
            modules(module {
                single { mockRepository }
                factory { NotesViewModel(get()) }
            })
        }
    }

    @After
    fun teardown() {
        stopKoin()
    }

    @Test
    fun test1_VerifyFormElementsExist() {
        composeTestRule.setContent { App() } // HAPUS PARAMETER, biarkan Koin yang bekerja!

        composeTestRule.onNodeWithText("Judul Catatan").assertExists()
        composeTestRule.onNodeWithText("Isi Catatan").assertExists()
        composeTestRule.onNodeWithText("Simpan Catatan").assertExists()
    }

    @Test
    fun test2_TypingInTitle_ShouldUpdateUiState() {
        composeTestRule.setContent { App() }

        composeTestRule.onNodeWithText("Judul Catatan").performTextInput("Belajar UI Test")
        composeTestRule.onNodeWithText("Belajar UI Test").assertExists()
    }

    @Test
    fun test3_ClickingSaveButton_ExecutesAction() {
        composeTestRule.setContent { App() }

        composeTestRule.onNodeWithText("Judul Catatan").performTextInput("Catatan Baru")
        composeTestRule.onNodeWithText("Isi Catatan").performTextInput("Isi Catatan Baru")
        composeTestRule.onNodeWithText("Simpan Catatan").performClick()
    }
}