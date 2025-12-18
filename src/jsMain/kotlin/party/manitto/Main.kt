package party.manitto

import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import kotlinx.browser.window
import party.manitto.auth.AuthState
import party.manitto.ui.*
import party.manitto.ui.theme.ManittoTheme

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    AuthState.init()
    
    CanvasBasedWindow(canvasElementId = "ComposeTarget") {
        ManittoTheme {
            App()
        }
    }
}

@Composable
fun App() {
    var currentPath by remember { mutableStateOf(window.location.hash.removePrefix("#")) }
    
    // URL 변경 감지
    DisposableEffect(Unit) {
        val listener: (dynamic) -> Unit = {
            currentPath = window.location.hash.removePrefix("#")
        }
        window.addEventListener("hashchange", listener)
        onDispose {
            window.removeEventListener("hashchange", listener)
        }
    }
    
    val navigate: (String) -> Unit = { path ->
        window.location.hash = "#$path"
        currentPath = path
    }
    
    if (AuthState.isLoading) return
    
    if (AuthState.user == null) {
        LoginScreen(onLoginSuccess = { /* AuthState handles it */ })
        return
    }
    
    // 라우팅
    when {
        currentPath.isEmpty() || currentPath == "/" -> {
            CreatePartyScreen(onNavigate = navigate)
        }
        currentPath.matches(Regex("/party/(\\d+)/join")) -> {
            val partyId = currentPath.substringAfter("/party/").substringBefore("/join")
            JoinPartyScreen(partyId = partyId, onNavigate = navigate)
        }
        currentPath.matches(Regex("/party/(\\d+)/status")) -> {
            val partyId = currentPath.substringAfter("/party/").substringBefore("/status")
            PartyStatusScreen(partyId = partyId, onNavigate = navigate)
        }
        currentPath.matches(Regex("/party/(\\d+)/result")) -> {
            val partyId = currentPath.substringAfter("/party/").substringBefore("/result")
            MatchResultScreen(partyId = partyId, onNavigate = navigate)
        }
        else -> {
            CreatePartyScreen(onNavigate = navigate)
        }
    }
}
