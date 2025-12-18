package party.manitto.ui

import androidx.compose.runtime.*
import kotlinx.browser.window
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.px
import party.manitto.api.ApiClient
import party.manitto.api.CreatePartyRequest
import party.manitto.api.PartyResponse

@Composable
fun CreatePartyScreen(onNavigate: (String) -> Unit) {
    var partyName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    val scope = MainScope()
    
    GradientBackground {
        Card {
            Title("🎉 마니또 방 만들기")
            
            Spacer(30.px)
            
            TextField(
                value = partyName,
                onValueChange = { partyName = it },
                placeholder = "방 이름"
            )
            
            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "비밀번호",
                type = InputType.Password
            )
            
            Spacer(5.px)
            
            PrimaryButton(
                text = if (isLoading) "생성 중..." else "방 만들기 ✨",
                enabled = !isLoading,
                onClick = {
                    if (partyName.isBlank() || password.isBlank()) {
                        window.alert("방 이름과 비밀번호를 입력해주세요!")
                        return@PrimaryButton
                    }
                    
                    isLoading = true
                    scope.launch {
                        try {
                            val response = ApiClient.post<CreatePartyRequest, PartyResponse>(
                                "/parties",
                                CreatePartyRequest(partyName, password)
                            )
                            window.alert("파티 생성 완료! ID: ${response.id}")
                            onNavigate("/party/${response.id}/status")
                        } catch (e: Exception) {
                            println("Error: ${e.message}")
                            window.alert("파티 생성 실패 😢")
                        } finally {
                            isLoading = false
                        }
                    }
                }
            )
        }
    }
}
