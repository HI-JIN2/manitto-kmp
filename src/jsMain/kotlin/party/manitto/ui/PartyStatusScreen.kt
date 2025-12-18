package party.manitto.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.browser.window
import kotlinx.coroutines.launch
import party.manitto.api.ApiClient
import party.manitto.api.Participant
import party.manitto.api.PartyStatus
import party.manitto.ui.components.*

@Composable
fun PartyStatusScreen(
    partyId: String,
    onNavigate: (String) -> Unit
) {
    var participants by remember { mutableStateOf<List<Participant>>(emptyList()) }
    var isMatched by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var isMatching by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    
    val scope = rememberCoroutineScope()
    
    // 데이터 로드
    LaunchedEffect(partyId) {
        try {
            participants = ApiClient.get("/parties/$partyId/participants")
            val status: PartyStatus = ApiClient.get("/parties/$partyId/status")
            isMatched = status.matched
        } catch (e: Exception) {
            println("Error: ${e.message}")
            message = "파티 정보를 불러올 수 없습니다 😢"
        } finally {
            isLoading = false
        }
    }
    
    GradientBackground {
        CardContainer {
            ScreenTitle("👥 파티 참가자 목록")
            
            Spacer(modifier = Modifier.height(20.dp))
            
            if (isLoading) {
                CircularProgressIndicator(color = Color(0xFF667eea))
            } else {
                // 초대 링크 복사
                SecondaryButton(
                    text = "초대 링크 복사 📋",
                    onClick = {
                        val link = "${window.location.origin}/#/party/$partyId/join"
                        window.navigator.clipboard.writeText(link)
                        window.alert("초대 링크가 복사되었습니다! 📋")
                    }
                )
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // 참가자 목록
                if (participants.isEmpty()) {
                    Text(
                        text = "아직 참가자가 없습니다.",
                        color = Color(0xFF666666)
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        participants.forEach { participant ->
                            ParticipantItem(email = participant.email)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                // 매칭 상태
                if (isMatched) {
                    SuccessMessage("🎁 이미 매칭이 완료된 파티입니다!")
                    
                    Spacer(modifier = Modifier.height(15.dp))
                    
                    PrimaryButton(
                        text = "내 마니또 확인하기",
                        onClick = { onNavigate("/party/$partyId/result") }
                    )
                } else {
                    PrimaryButton(
                        text = if (isMatching) "매칭 중..." else "매칭 시작 🎁",
                        onClick = {
                            isMatching = true
                            scope.launch {
                                try {
                                    ApiClient.postEmpty<Unit>("/parties/$partyId/match")
                                    message = "매칭 완료! 이메일이 발송되었습니다 ✉️"
                                    isMatched = true
                                } catch (e: Exception) {
                                    println("Error: ${e.message}")
                                    message = "매칭 실패 😢"
                                } finally {
                                    isMatching = false
                                }
                            }
                        },
                        enabled = !isMatching
                    )
                }
                
                // 메시지
                message?.let { msg ->
                    Spacer(modifier = Modifier.height(15.dp))
                    if (msg.contains("완료")) {
                        SuccessMessage(msg)
                    } else {
                        ErrorMessage(msg)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            
            NavLink(
                text = "← 홈으로",
                onClick = { onNavigate("/") }
            )
        }
    }
}

@Composable
private fun ParticipantItem(email: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFF8F9FA),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {
        Text(
            text = "👤 $email",
            fontSize = 14.sp,
            color = Color(0xFF555555)
        )
    }
}

