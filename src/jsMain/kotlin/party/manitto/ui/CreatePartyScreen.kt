package party.manitto.ui

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.browser.window
import kotlinx.coroutines.launch
import party.manitto.api.ApiClient
import party.manitto.api.CreatePartyRequest
import party.manitto.api.PartyResponse
import party.manitto.ui.components.*

@Composable
fun CreatePartyScreen(
    onNavigate: (String) -> Unit
) {
    var partyName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    
    GradientBackground {
        CardContainer {
            ScreenTitle("🎉 마니또 방 만들기")
            
            Spacer(modifier = Modifier.height(30.dp))
            
            ManittoTextField(
                value = partyName,
                onValueChange = { partyName = it },
                placeholder = "방 이름"
            )
            
            Spacer(modifier = Modifier.height(15.dp))
            
            ManittoTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = "비밀번호",
                isPassword = true
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            PrimaryButton(
                text = if (isLoading) "생성 중..." else "방 만들기 ✨",
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
                },
                enabled = !isLoading
            )
        }
    }
}

