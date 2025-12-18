package party.manitto.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import party.manitto.api.ApiClient
import party.manitto.api.MatchResult
import party.manitto.ui.components.*

@Composable
fun MatchResultScreen(
    partyId: String,
    onNavigate: (String) -> Unit
) {
    var result by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }
    
    val scope = rememberCoroutineScope()
    
    GradientBackground {
        CardContainer {
            ScreenTitle("🎁 당신의 마니또는...")
            
            Spacer(modifier = Modifier.height(30.dp))
            
            if (result != null) {
                ResultBox("🎉 $result 🎉")
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Text(
                    text = "이 사람에게 몰래 선물을 준비해보세요!",
                    fontSize = 14.sp,
                    color = Color(0xFF666666)
                )
            } else {
                PrimaryButton(
                    text = if (isLoading) "확인 중..." else "결과 보기 👀",
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
                    },
                    enabled = !isLoading
                )
            }
            
            // 에러 메시지
            error?.let {
                Spacer(modifier = Modifier.height(15.dp))
                ErrorMessage(it)
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            NavLink(
                text = "← 파티 상태로",
                onClick = { onNavigate("/party/$partyId/status") }
            )
        }
    }
}

