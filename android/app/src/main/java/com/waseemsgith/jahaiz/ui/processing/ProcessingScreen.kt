package com.waseemsgith.jahaiz.ui.processing

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kotlin.math.max
import androidx.hilt.navigation.compose.hiltViewModel
import com.waseemsgith.jahaiz.R
import com.waseemsgith.jahaiz.core.ui.theme.JahaizBlack
import com.waseemsgith.jahaiz.core.ui.theme.JahaizGold
import com.waseemsgith.jahaiz.core.ui.theme.JahaizMaroon
import com.waseemsgith.jahaiz.ui.components.LottieAnimationView

@Composable
fun ProcessingScreen(
    onDone: () -> Unit,
    onFailed: () -> Unit,
    modifier: Modifier = Modifier,
    vm: ProcessingViewModel = hiltViewModel(),
) {
    val messages = stringArrayResource(id = R.array.processing_msgs_dual)
    val step by vm.stepIndex.collectAsState()

    LaunchedEffect(Unit) {
        vm.start(
            done = { onDone() },
            failed = { onFailed() },
        )
    }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(listOf(JahaizBlack, JahaizMaroon.copy(alpha = 0.55f), JahaizBlack)),
                ),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LottieAnimationView(
                animationRes = R.raw.image_scan,
                modifier = Modifier.fillMaxWidth().height(220.dp),
                speed = 0.98f,
            )
            Spacer(Modifier.height(18.dp))

            val last = messages.lastIndex.coerceAtLeast(0)
            val idx = max(0, step - 1).coerceAtMost(last)
            val msg = messages.getOrNull(idx) ?: stringResource(id = R.string.loading_satire_dual)

            val alpha by animateFloatAsState(targetValue = 1f, animationSpec = tween(450), label = "alpha")
            Text(
                text = msg,
                style = MaterialTheme.typography.titleLarge,
                color = JahaizGold,
                textAlign = TextAlign.Center,
                modifier = Modifier.alpha(alpha),
            )

            Spacer(Modifier.height(18.dp))

            LinearProgressIndicator(
                progress = { (step.coerceIn(0, 5)) / 5f },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                trackColor = Color.White.copy(alpha = 0.12f),
                color = JahaizGold,
            )

            Text(
                text = stringResource(id = R.string.loading_engine_dual),
                color = Color.LightGray.copy(alpha = 0.75f),
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 14.dp),
            )
        }
    }
}
