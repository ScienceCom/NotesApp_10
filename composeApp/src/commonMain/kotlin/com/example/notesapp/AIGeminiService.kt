package com.example.notesapp

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.serialization.Serializable

class GeminiService(private val client: HttpClient) {
    private val apiKey = "AIzaSyDhuRo4_sMrCbQD7PZr2tGFjUzeLV2Zask"

    private val endpoint = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=$apiKey"

    suspend fun getAiSummary(noteContent: String): String {
        if (noteContent.isBlank()) return "Tulis catatan dulu bos."

        return try {
            val response = client.post(endpoint) {
                contentType(ContentType.Application.Json)
                setBody(GeminiRequest(
                    contents = listOf(Content(parts = listOf(Part(text = "Rangkum teks ini jadi poin-poin singkat: $noteContent"))))
                ))
            }

            if (response.status == HttpStatusCode.OK) {
                val data = response.body<GeminiResponse>()
                data.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "AI-nya diem aja nih."
            } else {
                "Waduh Error ${response.status.value}: ${response.bodyAsText()}"
            }
        } catch (e: Exception) {
            "Kesalahan: ${e.message}"
        }
    }
}


@Serializable data class GeminiRequest(val contents: List<Content>)
@Serializable data class Content(val parts: List<Part>)
@Serializable data class Part(val text: String)
@Serializable data class GeminiResponse(val candidates: List<Candidate>? = null)
@Serializable data class Candidate(val content: Content)
