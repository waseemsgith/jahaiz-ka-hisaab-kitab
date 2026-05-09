package com.waseemsgith.jahaiz.ui.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.waseemsgith.jahaiz.R
import com.waseemsgith.jahaiz.core.ui.theme.JahaizBlack
import com.waseemsgith.jahaiz.core.ui.theme.JahaizGold
import com.waseemsgith.jahaiz.core.ui.theme.JahaizMaroon
import com.waseemsgith.jahaiz.ui.components.LottieAnimationView
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@Composable
fun SatireLoadingScreen(onDone: () -> Unit, modifier: Modifier = Modifier) {
    val jokes = stringArrayResource(id = R.array.satire_jokes_dual)
    var idx by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        coroutineScope {
            val ticker =
                launch {
                    var cursor = 0
                    while (isActive && jokes.isNotEmpty()) {
                        delay(1_520)
                        cursor = (cursor + 1) % jokes.size
                        idx = cursor
                    }
                }
            delay(3_200)
            ticker.cancel()
            ticker.join()
            onDone()
        }
    }

    Box(
        modifier =
            modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(JahaizBlack, JahaizMaroon.copy(alpha = 0.6f), JahaizBlack))),
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center).padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LottieAnimationView(
                animationRes = R.raw.loading_ai,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp),
            )
            Spacer(modifier = Modifier.height(22.dp))

            val text =
                jokes.getOrNull(idx.coerceIn(0, jokes.lastIndex.coerceAtLeast(0)))
                    ?: stringResource(id = R.string.loading_satire_dual)
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium,
                color = JahaizGold,
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = JahaizGold,
                trackColor = Color.White.copy(alpha = 0.12f),
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
