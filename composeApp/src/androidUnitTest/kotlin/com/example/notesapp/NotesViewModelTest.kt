package com.example.notesapp

import app.cash.turbine.test
import com.example.notesapp.data.NotesRepository
import com.example.notesapp.data.NotesViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import com.example.notesapp.database.Note

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelTest {

    private val mockRepository = mockk<NotesRepository>(relaxed = true)

    // Diubah ke UnconfinedTestDispatcher agar coroutine langsung dieksekusi tanpa advanceUntilIdle()
    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: NotesViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        val dummyNotes = listOf(Note(1L, "Tugas 10", "Selesai Praktikum", 0L))
        coEvery { mockRepository.getAllNotes() } returns flowOf(dummyNotes)
        viewModel = NotesViewModel(mockRepository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun test1_LoadNotes_ShouldCallRepository() = runTest {
        viewModel.loadNotes()
        // advanceUntilIdle() sudah tidak diperlukan lagi di sini
        coVerify { mockRepository.getAllNotes() }
    }

    @Test
    fun test2_InsertNote_ShouldCallRepositoryInsert() = runTest {
        viewModel.insertNote("Judul", "Konten")
        coVerify { mockRepository.insertNote("Judul", "Konten") }
    }

    @Test
    fun test3_DeleteNote_ShouldCallRepositoryDelete() = runTest {
        viewModel.deleteNote(99L)
        coVerify { mockRepository.deleteNote(99L) }
    }

    @Test
    fun test4_InitialState_ShouldBeEmpty_BeforeRepositoryEmits() = runTest {
        val emptyRepo = mockk<NotesRepository>(relaxed = true)
        coEvery { emptyRepo.getAllNotes() } returns flowOf(emptyList())
        val emptyViewModel = NotesViewModel(emptyRepo)
        assertEquals(emptyList<Note>(), emptyViewModel.notes.value)
    }

    @Test
    fun test5_NotesFlow_EmitsInitialStateAndUpdatedData() = runTest {
        viewModel.loadNotes()

        viewModel.notes.test {
            val item = awaitItem()

            val finalItem = if (item.isEmpty()) awaitItem() else item

            assertEquals("Tugas 10", finalItem[0].title)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun test6_NotesFlow_ShouldBeEmpty_WhenRepositoryIsEmpty() = runTest {
        val emptyRepo = mockk<NotesRepository>(relaxed = true)
        coEvery { emptyRepo.getAllNotes() } returns flowOf(emptyList())
        val emptyViewModel = NotesViewModel(emptyRepo)

        emptyViewModel.notes.test {
            val item = awaitItem()
            assertEquals(true, item.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }
}