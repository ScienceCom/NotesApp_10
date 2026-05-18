package com.example.notesapp.di

import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.notesapp.database.DatabaseDriverFactory
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.platform.NetworkMonitor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.core.module.Module

actual val platformModule: Module = module {
    single {
        // Menyediakan Driver SQLDelight Android ke dalam Koin
        val driver = AndroidSqliteDriver(NotesDatabase.Schema, androidContext(), "Notes.db")
        NotesDatabase(driver)
    }
}