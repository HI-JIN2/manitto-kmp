package party.manitto.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.browser.window

@Composable
fun GoogleLoginButton() {
    var isLoading by remember { mutableStateOf(false) }
    
    DisposableEffect(Unit) {
        val env = window.asDynamic().ENV
        val clientId = env?.GOOGLE_CLIENT_ID as? String 
            ?: "772988401705-4io9tviphr75k075kb9lbrnmn960h2r8.apps.googleusercontent.com"
        
        // Google Sign-In 초기화
        val google = window.asDynamic().google
        if (google != null && google.accounts != null) {
            google.accounts.id.initialize(
                js("({client_id: clientId, callback: function(response) { window.handleGoogleLogin(response.credential); }})")
            )
        }
        
        // Kotlin callback 등록
        window.asDynamic().handleGoogleLogin = { credential: String ->
            isLoading = true
            AuthState.loginWithGoogle(credential)
        }
        
        onDispose {
            window.asDynamic().handleGoogleLogin = null
        }
    }
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color(0xFF667eea),
                modifier = Modifier.size(40.dp)
            )
        } else {
            Button(
                onClick = {
                    val google = window.asDynamic().google
                    if (google != null && google.accounts != null) {
                        google.accounts.id.prompt()
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF333333)
                ),
                modifier = Modifier.height(50.dp)
            ) {
                Text("🔐 Google로 로그인")
            }
        }
    }
}
