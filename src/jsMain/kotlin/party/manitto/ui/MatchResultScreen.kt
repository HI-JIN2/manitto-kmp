package party.manitto.ui

import androidx.compose.runtime.*
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.px
import party.manitto.api.ApiClient
import party.manitto.api.MatchResult

@Composable
fun MatchResultScreen(partyId: String, onNavigate: (String) -> Unit) {
    var result by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    
    val scope = MainScope()
    
    GradientBackground {
        Card {
            Title("🎁 당신의 마니또는...")
            
            Spacer(30.px)
            
            if (result != null) {
                ResultBox("🎉 $result 🎉")
                
                Spacer(20.px)
                
                Subtitle("이 사람에게 몰래 선물을 준비해보세요!")
            } else {
                PrimaryButton(
                    text = if (isLoading) "확인 중..." else "결과 보기 👀",
                    enabled = !isLoading,
                    onClick = {
                        isLoading = true
                        error = null
                        scope.launch {
                            try {
                                val response: MatchResult = ApiClient.get("/parties/match/result")
                                result = response.receiver
                            } catch (e: Exception) {
                                println("Error: ${e.message}")
                                error = "결과를 불러올 수 없습니다 😢"
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                )
            }
            
            error?.let { ErrorMessage(it) }
            
            Spacer(20.px)
            NavLink("← 파티 상태로") { onNavigate("/party/$partyId/status") }
        }
    }
}
