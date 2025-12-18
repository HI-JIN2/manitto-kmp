package party.manitto.auth

import androidx.compose.runtime.*
import kotlinx.browser.document
import kotlinx.browser.window
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

@Composable
fun GoogleLoginButton() {
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    
    LaunchedEffect(Unit) {
        // Kotlin callback 등록
        window.asDynamic().handleGoogleLogin = { credential: String ->
            isLoading = true
            AuthState.loginWithGoogle(credential)
        }
        
        // Google Sign-In 초기화
        window.setTimeout({
            initGoogleSignIn { err ->
                error = err
            }
        }, 1000)
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
        // Google 버튼 컨테이너
        Div({
            id("google-btn")
            style {
                display(DisplayStyle.Flex)
                justifyContent(JustifyContent.Center)
                minHeight(50.px)
            }
        })
        
        // 에러 메시지
        error?.let { err ->
            Div({
                style {
                    color(Color("#dc3545"))
                    fontSize(12.px)
                    marginTop(10.px)
                }
            }) {
                Text(err)
            }
        }
    }
}

private fun initGoogleSignIn(onError: (String?) -> Unit) {
    val env = window.asDynamic().ENV
    val clientId = env?.GOOGLE_CLIENT_ID as? String
    
    println("ENV: $env")
    println("Client ID: $clientId")
    
    if (clientId.isNullOrBlank()) {
        onError("Google Client ID가 설정되지 않았습니다")
        println("ERROR: Google Client ID not configured")
        return
    }
    
    val google = window.asDynamic().google
    println("Google SDK: $google")
    
    if (google == null || google.accounts == null) {
        onError("Google SDK 로딩 실패")
        println("ERROR: Google SDK not loaded")
        return
    }
    
    try {
        val config = js("{}")
        config.client_id = clientId
        config.callback = js("(function(r) { console.log('Google callback:', r); window.handleGoogleLogin(r.credential); })")
        google.accounts.id.initialize(config)
        println("Google initialized")
        
        val btnElement = document.getElementById("google-btn")
        println("Button element: $btnElement")
        
        if (btnElement != null) {
            val btnConfig = js("{}")
            btnConfig.theme = "outline"
            btnConfig.size = "large"
            btnConfig.text = "signin_with"
            btnConfig.width = 280
            google.accounts.id.renderButton(btnElement, btnConfig)
            println("Button rendered")
            onError(null)
        } else {
            onError("버튼 요소를 찾을 수 없습니다")
        }
    } catch (e: Exception) {
        println("ERROR: ${e.message}")
        onError("초기화 실패: ${e.message}")
    }
}
