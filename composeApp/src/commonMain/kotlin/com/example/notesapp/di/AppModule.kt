package com.example.notesapp.di

import com.example.notesapp.GeminiService
import com.example.notesapp.data.NotesRepository
import com.example.notesapp.data.NotesViewModel
import com.example.notesapp.platform.DeviceInfo
import org.koin.dsl.module


val dataModule = module {
    // Asumsi NotesDatabase sudah di-inject dari platform (Android/iOS)
    single { NotesRepository(get()) }

    factory { DeviceInfo() }

}

val viewModelModule = module {
    factory { NotesViewModel(get()) }

    single { GeminiService(client = get()) }
}

val appModule = listOf(dataModule, viewModelModule) + platformModule