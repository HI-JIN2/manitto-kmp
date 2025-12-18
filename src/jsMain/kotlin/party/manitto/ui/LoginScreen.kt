package party.manitto.ui

import androidx.compose.runtime.Composable
import party.manitto.auth.GoogleLoginButton

@Composable
fun LoginScreen() {
    GradientBackground {
        Card {
            Title("🎁 마니또에 오신 것을 환영합니다!")
            Subtitle("Google 계정으로 로그인하여 마니또 파티를 시작하세요")
            GoogleLoginButton()
        }
    }
}
