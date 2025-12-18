package party.manitto.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.browser.window
import kotlinx.coroutines.launch
import party.manitto.api.ApiClient
import party.manitto.api.JoinPartyRequest
import party.manitto.auth.AuthState
import party.manitto.ui.components.*

@Composable
fun JoinPartyScreen(
    partyId: String,
    onNavigate: (String) -> Unit
) {
    var isLoading by remember { mutableStateOf(false) }
    var joined by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    val user = AuthState.user
    
    GradientBackground {
        CardContainer {
            ScreenTitle("🔑 마니또 방 참여")
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "파티 ID: $partyId",
                color = Color(0xFF666666)
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            if (joined) {
                SuccessMessage("🎈 참여 완료!")
                
                Spacer(modifier = Modifier.height(15.dp))
                
                PrimaryButton(
                    text = "파티 상태 보기",
                    onClick = { onNavigate("/party/$partyId/status") }
                )
            } else {
                PrimaryButton(
                    text = if (isLoading) "참여 중..." else "참여하기 🎈",
                    onClick = {
                        if (user == null) {
                            window.alert("로그인이 필요합니다 😢")
                            return@PrimaryButton
                        }
                        
                        isLoading = true
                        scope.launch {
                            try {
                                ApiClient.post<JoinPartyRequest, Unit>(
                                    "/parties/$partyId/join",
                                    JoinPartyRequest(user.sub)
                                )
                                joined = true
                            } catch (e: Exception) {
                                println("Error: ${e.message}")
                                window.alert("참여 실패 😢")
                            } finally {
                                isLoading = false
                            }
                        }
                    },
                    enabled = !isLoading
                )
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            NavLink(
                text = "← 홈으로",
                onClick = { onNavigate("/") }
            )
        }
    }
}

