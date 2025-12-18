package party.manitto.auth

import androidx.compose.runtime.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun GoogleLoginButton() {
    var isLoading by remember { mutableStateOf(false) }
    
    DisposableEffect(Unit) {
        // Kotlin callback 등록
        window.asDynamic().handleGoogleLogin = { credential: String ->
            isLoading = true
            AuthState.loginWithGoogle(credential)
        }
        
        // Google Sign-In 초기화
        window.setTimeout({
            val env = window.asDynamic().ENV
            val clientId = (env?.GOOGLE_CLIENT_ID as? String)?.takeIf { it.isNotBlank() }
                ?: return@setTimeout
            
            val google = window.asDynamic().google
            if (google != null && google.accounts != null) {
                val config = js("{}")
                config.client_id = clientId
                config.callback = js("(function(r) { window.handleGoogleLogin(r.credential); })")
                google.accounts.id.initialize(config)
                
                val btnConfig = js("{}")
                btnConfig.theme = "outline"
                btnConfig.size = "large"
                btnConfig.text = "signin_with"
                google.accounts.id.renderButton(document.getElementById("google-btn"), btnConfig)
            }
        }, 500)
        
        onDispose {
            window.asDynamic().handleGoogleLogin = null
        }
    }
    
    if (isLoading) {
        Div({
            style {
                fontSize(18.px)
                color(Color("#667eea"))
            }
        }) {
            Text("⏳ 로그인 중...")
        }
    } else {
        Div({
            id("google-btn")
            style {
                display(DisplayStyle.Flex)
                justifyContent(JustifyContent.Center)
            }
        })
    }
}
