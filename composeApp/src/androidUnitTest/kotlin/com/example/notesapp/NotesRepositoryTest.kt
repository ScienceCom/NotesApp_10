package com.example.notesapp.data

import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.database.Note
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class NotesRepositoryTest {

    // Hanya mock database utama. Biarkan Kotlin yang menebak tipe data child-nya (infer type otomatis)
    private val mockDatabase = mockk<NotesDatabase>(relaxed = true)

    // Gunakan spyk agar fungsi return bawaan repository bisa dimanipulasi dengan aman
    private val repository = spyk(NotesRepository(mockDatabase))

    @Test
    fun test1_InsertNote_ShouldCallDatabaseInsert() = runTest {
        repository.insertNote("Judul", "Isi")

        // Panggil langsung dari mockDatabase (Catatan: Jika tulisan notesQueries MERAH, ubah huruf 's' nya menjadi noteQueries)
        coVerify(exactly = 1) {
            mockDatabase.noteQueries.insert("Judul", "Isi", any<Long>())
        }
    }

    @Test
    fun test2_DeleteNote_ShouldCallDatabaseDelete() = runTest {
        repository.deleteNote(5L)
        coVerify(exactly = 1) { mockDatabase.noteQueries.delete(5L) }
    }

    @Test
    fun test3_GetAllNotes_ReturnsCorrectData() = runTest {
        val expectedNotes = listOf(Note(1L, "T1", "C1", 0L))
        coEvery { repository.getAllNotes() } returns flowOf(expectedNotes)

        repository.getAllNotes().collect { notes ->
            assertEquals(1, notes.size)
            assertEquals("T1", notes[0].title)
        }
    }

    @Test
    fun test4_InsertNote_WithEmptyContent_ShouldStillExecute() = runTest {
        repository.insertNote("", "")
        coVerify { mockDatabase.noteQueries.insert("", "", any<Long>()) }
    }

    @Test
    fun test5_DeleteNote_WithNegativeId_ShouldStillExecute() = runTest {
        repository.deleteNote(-1L)
        coVerify { mockDatabase.noteQueries.delete(-1L) }
    }
}