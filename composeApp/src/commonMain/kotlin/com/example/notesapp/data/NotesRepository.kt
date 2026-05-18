package com.example.notesapp.data

import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.database.Note
import kotlinx.coroutines.flow.Flow
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlin.time.ExperimentalTime

class NotesRepository(private val database: NotesDatabase) {

    private val queries = database.noteQueries

    // 1. Ambil Semua Catatan (Menggunakan selectAll sesuai .sq)
    fun getAllNotes(): Flow<List<Note>> {
        return queries.selectAll()
            .asFlow()
            // Kita paksa menentukan tipe data objek 'Note' secara eksplisit di sini
            .mapToList(Dispatchers.IO)
    }

    // 2. Tambah Catatan (Menggunakan insert sesuai .sq)
    suspend fun insertNote(title: String, content: String) {
        val currentTime = clockTime()
        queries.insert(
            title = title,
            content = content,
            created_at = currentTime
        )
    }

    // 3. Hapus Catatan (Menggunakan delete sesuai .sq)
    suspend fun deleteNote(id: Long) {
        queries.delete(id)
    }

    @OptIn(ExperimentalTime::class)
    private fun clockTime(): Long {
        // Panggil Clock.System murni milik Kotlin Multiplatform
        return kotlin.time.Clock.System.now().toEpochMilliseconds()
    }
}