package party.manitto.ui

import kotlinx.browser.window

object KakaoShare {
    private var initialized = false
    
    fun init() {
        if (initialized) return
        
        val kakaoKey = window.asDynamic().ENV?.KAKAO_JS_KEY as? String
        if (kakaoKey.isNullOrBlank()) {
            println("Kakao JS Key not configured")
            return
        }
        
        val kakao = window.asDynamic().Kakao
        if (kakao != null && kakao.isInitialized() == false) {
            kakao.init(kakaoKey)
            initialized = true
            println("Kakao SDK initialized")
        }
    }
    
    fun sharePartyInvite(partyId: String, partyName: String = "마니또 파티") {
        init()
        
        val kakao = window.asDynamic().Kakao
        if (kakao == null || kakao.Share == null) {
            // 카카오 SDK 없으면 일반 공유로 대체
            fallbackShare(partyId)
            return
        }
        
        val inviteUrl = "${window.location.origin}/#/party/$partyId/join"
        
        try {
            // 동적 객체 생성
            val content = js("{}")
            content.title = "🎁 마니또 파티에 초대합니다!"
            content.description = "$partyName 에 참여해서 마니또가 되어보세요!"
            content.imageUrl = "https://em-content.zobj.net/source/apple/354/wrapped-gift_1f381.png"
            
            val link = js("{}")
            link.mobileWebUrl = inviteUrl
            link.webUrl = inviteUrl
            content.link = link
            
            val button = js("{}")
            button.title = "파티 참여하기"
            button.link = link
            
            val buttons = js("[]")
            buttons.push(button)
            
            val shareParams = js("{}")
            shareParams.objectType = "feed"
            shareParams.content = content
            shareParams.buttons = buttons
            
            kakao.Share.sendDefault(shareParams)
        } catch (e: Exception) {
            println("Kakao share failed: ${e.message}")
            fallbackShare(partyId)
        }
    }
    
    private fun fallbackShare(partyId: String) {
        val inviteUrl = "${window.location.origin}/#/party/$partyId/join"
        
        // Web Share API 시도
        val navigator = window.navigator.asDynamic()
        if (navigator.share != undefined) {
            val shareData = js("{}")
            shareData.title = "🎁 마니또 파티 초대"
            shareData.text = "마니또 파티에 참여해보세요!"
            shareData.url = inviteUrl
            navigator.share(shareData)
        } else {
            // 클립보드 복사
            window.navigator.clipboard.writeText(inviteUrl)
            window.alert("초대 링크가 복사되었습니다!\n$inviteUrl")
        }
    }
}
