package party.manitto.api

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.serialization.json.Json

object ApiClient {
    val baseUrl: String
        get() {
            val env = window.asDynamic().ENV
            return env?.API_BASE_URL as? String ?: "http://localhost:8080"
        }
    
    val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
    
    fun getToken(): String? = localStorage.getItem("token")
    
    suspend inline fun <reified T> get(endpoint: String): T {
        return client.get("$baseUrl$endpoint") {
            getToken()?.let { 
                header(HttpHeaders.Authorization, "Bearer $it")
            }
        }.body()
    }
    
    suspend inline fun <reified T, reified R> post(endpoint: String, body: T): R {
        return client.post("$baseUrl$endpoint") {
            contentType(ContentType.Application.Json)
            setBody(body)
            getToken()?.let {
                header(HttpHeaders.Authorization, "Bearer $it")
            }
        }.body()
    }
    
    suspend inline fun <reified R> postEmpty(endpoint: String): R {
        return client.post("$baseUrl$endpoint") {
            getToken()?.let {
                header(HttpHeaders.Authorization, "Bearer $it")
            }
        }.body()
    }
}
