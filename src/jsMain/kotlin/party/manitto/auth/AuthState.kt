package party.manitto.auth

import androidx.compose.runtime.*
import kotlinx.browser.localStorage
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import party.manitto.api.ApiClient
import party.manitto.api.AuthResponse
import party.manitto.api.GoogleAuthRequest

data class User(
    val sub: String,
    val email: String? = null
)

object AuthState {
    var user by mutableStateOf<User?>(null)
        private set
    
    var token by mutableStateOf<String?>(null)
        private set
    
    var isLoading by mutableStateOf(true)
        private set
    
    private val scope = MainScope()
    
    fun init() {
        val savedToken = localStorage.getItem("token")
        if (savedToken != null) {
            try {
                val payload = decodeJwt(savedToken)
                token = savedToken
                user = User(sub = payload.sub, email = payload.email)
            } catch (e: Exception) {
                localStorage.removeItem("token")
            }
        }
        isLoading = false
    }
    
    fun loginWithGoogle(credential: String) {
        scope.launch {
            try {
                val response = ApiClient.post<GoogleAuthRequest, AuthResponse>(
                    "/auth/google",
                    GoogleAuthRequest(credential)
                )
                localStorage.setItem("token", response.token)
                token = response.token
                
                val payload = decodeJwt(response.token)
                user = User(sub = payload.sub, email = payload.email)
            } catch (e: Exception) {
                println("Login failed: ${e.message}")
                window.alert("로그인 실패 😢")
            }
        }
    }
    
    fun logout() {
        localStorage.removeItem("token")
        token = null
        user = null
    }
    
    private data class JwtPayload(val sub: String, val email: String? = null)
    
    private fun decodeJwt(token: String): JwtPayload {
        val parts = token.split(".")
        if (parts.size != 3) throw IllegalArgumentException("Invalid JWT")
        
        val payload = window.atob(parts[1].replace("-", "+").replace("_", "/"))
        val json = JSON.parse<dynamic>(payload)
        
        return JwtPayload(
            sub = json.sub as String,
            email = json.email as? String
        )
    }
}
