package party.manitto.ui

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.*
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*

// ==================== Layout ====================

@Composable
fun GradientBackground(content: @Composable () -> Unit) {
    Div({
        style {
            minHeight(100.vh)
            display(DisplayStyle.Flex)
            alignItems(AlignItems.Center)
            justifyContent(JustifyContent.Center)
            background("linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
            padding(20.px)
        }
    }) {
        content()
    }
}

@Composable
fun Card(content: @Composable () -> Unit) {
    Div({
        style {
            backgroundColor(Color.white)
            borderRadius(20.px)
            padding(40.px)
            property("box-shadow", "0 10px 40px rgba(0,0,0,0.2)")
            minWidth(350.px)
            maxWidth(450.px)
            textAlign("center")
        }
    }) {
        content()
    }
}

@Composable
fun Spacer(height: CSSNumeric) {
    Div({ style { this.height(height) } })
}

// ==================== Typography ====================

@Composable
fun Title(text: String) {
    H1({
        style {
            fontSize(28.px)
            fontWeight(700)
            color(Color("#333"))
            marginBottom(10.px)
            marginTop(0.px)
        }
    }) {
        Text(text)
    }
}

@Composable
fun Subtitle(text: String) {
    P({
        style {
            color(Color("#666"))
            marginBottom(30.px)
        }
    }) {
        Text(text)
    }
}

// ==================== Inputs ====================

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    type: InputType<String> = InputType.Text
) {
    Input(type) {
        style {
            width(100.percent)
            padding(15.px)
            fontSize(16.px)
            border(1.px, LineStyle.Solid, Color("#ddd"))
            borderRadius(10.px)
            marginBottom(15.px)
            property("outline", "none")
            property("box-sizing", "border-box")
        }
        value(value)
        placeholder(placeholder)
        onInput { onValueChange(it.value) }
    }
}

// ==================== Buttons ====================

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button({
        style {
            width(100.percent)
            padding(15.px)
            fontSize(16.px)
            fontWeight(600)
            color(Color.white)
            backgroundColor(if (enabled) Color("#667eea") else Color("#ccc"))
            border(0.px)
            borderRadius(10.px)
            cursor(if (enabled) "pointer" else "not-allowed")
            property("transition", "all 0.3s")
        }
        onClick { if (enabled) onClick() }
        if (!enabled) disabled()
    }) {
        Text(text)
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit
) {
    Button({
        style {
            width(100.percent)
            padding(15.px)
            fontSize(16.px)
            fontWeight(600)
            color(Color("#667eea"))
            backgroundColor(Color.transparent)
            border(2.px, LineStyle.Solid, Color("#667eea"))
            borderRadius(10.px)
            cursor("pointer")
            marginTop(10.px)
            property("transition", "all 0.3s")
        }
        onClick { onClick() }
    }) {
        Text(text)
    }
}

@Composable
fun KakaoShareButton(
    text: String = "카카오톡으로 공유 💬",
    onClick: () -> Unit
) {
    Button({
        style {
            width(100.percent)
            padding(15.px)
            fontSize(16.px)
            fontWeight(600)
            color(Color("#3C1E1E"))
            backgroundColor(Color("#FEE500"))
            border(0.px)
            borderRadius(10.px)
            cursor("pointer")
            marginTop(10.px)
            property("transition", "all 0.3s")
        }
        onClick { onClick() }
    }) {
        Text(text)
    }
}

@Composable
fun NavLink(text: String, onClick: () -> Unit) {
    A(attrs = {
        style {
            color(Color("#667eea"))
            property("text-decoration", "none")
            fontSize(14.px)
            marginTop(20.px)
            display(DisplayStyle.InlineBlock)
            cursor("pointer")
        }
        onClick { onClick() }
    }) {
        Text(text)
    }
}

// ==================== Messages ====================

@Composable
fun SuccessMessage(text: String) {
    Div({
        style {
            color(Color("#28a745"))
            fontWeight(600)
            marginTop(20.px)
            padding(15.px)
            backgroundColor(Color("#d4edda"))
            borderRadius(10.px)
        }
    }) {
        Text(text)
    }
}

@Composable
fun ErrorMessage(text: String) {
    Div({
        style {
            color(Color("#dc3545"))
            fontWeight(600)
            marginTop(20.px)
            padding(15.px)
            backgroundColor(Color("#f8d7da"))
            borderRadius(10.px)
        }
    }) {
        Text(text)
    }
}

@Composable
fun ResultBox(text: String) {
    Div({
        style {
            marginTop(30.px)
            padding(25.px)
            backgroundColor(Color("#fff3cd"))
            borderRadius(15.px)
            fontSize(24.px)
            fontWeight(700)
            color(Color("#856404"))
        }
    }) {
        Text(text)
    }
}

// ==================== List ====================

@Composable
fun ParticipantItem(email: String) {
    Div({
        style {
            padding(12.px)
            backgroundColor(Color("#f8f9fa"))
            borderRadius(8.px)
            marginBottom(8.px)
            fontSize(14.px)
            color(Color("#555"))
            textAlign("left")
        }
    }) {
        Text("👤 $email")
    }
}

@Composable
fun LoadingSpinner() {
    Div({
        style {
            fontSize(24.px)
        }
    }) {
        Text("⏳ 로딩 중...")
    }
}
