package party.manitto.ui

import androidx.compose.runtime.*
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.px
import party.manitto.api.ApiClient
import party.manitto.api.JoinPartyRequest
import party.manitto.auth.AuthState

@Composable
fun JoinPartyScreen(partyId: String, onNavigate: (String) -> Unit) {
    var isLoading by remember { mutableStateOf(false) }
    var joined by remember { mutableStateOf(false) }
    
    val scope = MainScope()
    val user = AuthState.user
    
    GradientBackground {
        Card {
            Title("🔑 마니또 방 참여")
            Subtitle("파티 ID: $partyId")
            
            if (joined) {
                SuccessMessage("🎈 참여 완료!")
                
                Spacer(15.px)
                
                PrimaryButton(
                    text = "파티 상태 보기",
                    onClick = { onNavigate("/party/$partyId/status") }
                )
            } else {
                PrimaryButton(
                    text = if (isLoading) "참여 중..." else "참여하기 🎈",
                    enabled = !isLoading,
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
                    }
                )
            }
            
            Spacer(20.px)
            NavLink("← 홈으로") { onNavigate("/") }
        }
    }
}
