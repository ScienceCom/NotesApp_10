package com.example.notesapp.di

import com.example.notesapp.database.DatabaseDriverFactory
import com.example.notesapp.platform.DeviceInfo
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.GeminiService
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformModule: Module

val appModule = module {
    // 1. Setup HttpClient (Ktor) untuk koneksi API
    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    coerceInputValues = true
                })
            }
        }
    }

    // 2. Setup GeminiService untuk fitur AI
    single { GeminiService(get()) }

    // 3. Setup Database (Lama)
    single {
        val driverFactory = get<DatabaseDriverFactory>()
        NotesDatabase(driverFactory.createDriver())
    }

    // 4. Setup Device Info (Lama)
    single { DeviceInfo() }
}