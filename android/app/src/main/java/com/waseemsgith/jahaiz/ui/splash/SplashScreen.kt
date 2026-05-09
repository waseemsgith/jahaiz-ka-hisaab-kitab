package com.waseemsgith.jahaiz.ui.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.waseemsgith.jahaiz.R
import com.waseemsgith.jahaiz.core.ui.theme.JahaizBlack
import com.waseemsgith.jahaiz.core.ui.theme.JahaizGold
import com.waseemsgith.jahaiz.ui.components.LottieAnimationView
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onDone: () -> Unit, modifier: Modifier = Modifier) {
    val zoom = remember { Animatable(1.06f) }
    val fade = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        delay(2_050)
        zoom.animateTo(targetValue = 1.35f, animationSpec = tween(650))
        fade.animateTo(targetValue = 0f, animationSpec = tween(260))
        onDone()
    }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(JahaizBlack, JahaizBlack.copy(alpha = 0.92f)))),
    ) {
        Column(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = 18.dp)
                    .scale(zoom.value)
                    .alpha(fade.value),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LottieAnimationView(
                animationRes = R.raw.splash_animation,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                speed = 1.05f,
            )
            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = "🪙🖩",
                style = MaterialTheme.typography.displayLarge.copy(color = JahaizGold),
                textAlign = TextAlign.Center,
            )
            Text(
                text = stringResource(id = R.string.app_title_urdu_line),
                style = MaterialTheme.typography.titleLarge,
                color = JahaizGold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 6.dp),
            )
            Text(
                text = stringResource(id = R.string.tagline_bilingual_line),
                style = MaterialTheme.typography.bodyLarge,
                color = JahaizGold.copy(alpha = 0.78f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}
