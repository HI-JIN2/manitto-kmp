package party.manitto.ui

import androidx.compose.runtime.*
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.css.px
import party.manitto.api.ApiClient
import party.manitto.api.Participant
import party.manitto.api.PartyStatus

@Composable
fun PartyStatusScreen(partyId: String, onNavigate: (String) -> Unit) {
    var participants by remember { mutableStateOf<List<Participant>>(emptyList()) }
    var isMatched by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }
    var isMatching by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }
    
    val scope = MainScope()
    
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
        Card {
            Title("👥 파티 참가자 목록")
            
            Spacer(20.px)
            
            if (isLoading) {
                LoadingSpinner()
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
                
                Spacer(20.px)
                
                // 참가자 목록
                if (participants.isEmpty()) {
                    Subtitle("아직 참가자가 없습니다.")
                } else {
                    participants.forEach { participant ->
                        ParticipantItem(participant.email)
                    }
                }
                
                Spacer(20.px)
                
                // 매칭 상태
                if (isMatched) {
                    SuccessMessage("🎁 이미 매칭이 완료된 파티입니다!")
                    
                    Spacer(15.px)
                    
                    PrimaryButton(
                        text = "내 마니또 확인하기",
                        onClick = { onNavigate("/party/$partyId/result") }
                    )
                } else {
                    PrimaryButton(
                        text = if (isMatching) "매칭 중..." else "매칭 시작 🎁",
                        enabled = !isMatching,
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
                        }
                    )
                }
                
                // 메시지
                message?.let { msg ->
                    Spacer(15.px)
                    if (msg.contains("완료")) {
                        SuccessMessage(msg)
                    } else {
                        ErrorMessage(msg)
                    }
                }
            }
            
            Spacer(20.px)
            NavLink("← 홈으로") { onNavigate("/") }
        }
    }
}
