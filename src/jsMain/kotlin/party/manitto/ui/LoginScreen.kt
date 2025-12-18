package party.manitto.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import party.manitto.auth.GoogleLoginButton
import party.manitto.ui.components.*

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit
) {
    GradientBackground {
        CardContainer {
            ScreenTitle("🎁 마니또에 오신 것을 환영합니다!")
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Google 계정으로 로그인하여\n마니또 파티를 시작하세요",
                color = Color(0xFF666666)
            )
            
            Spacer(modifier = Modifier.height(30.dp))
            
            GoogleLoginButton()
        }
    }
}

