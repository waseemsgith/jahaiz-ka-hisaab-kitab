package com.waseemsgith.jahaiz.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waseemsgith.jahaiz.R
import com.waseemsgith.jahaiz.core.ui.theme.JahaizAccent
import com.waseemsgith.jahaiz.core.ui.theme.JahaizBlack
import com.waseemsgith.jahaiz.core.ui.theme.JahaizGold
import com.waseemsgith.jahaiz.ui.components.AnimatedLongCounter
import com.waseemsgith.jahaiz.ui.components.GlassCard
import com.waseemsgith.jahaiz.ui.components.LottieAnimationView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onStart: () -> Unit,
    modifier: Modifier = Modifier,
    vm: HomeViewModel = hiltViewModel(),
) {
    val analyzed by vm.analyzedCount.collectAsState()
    val crs by vm.rupeeCr.collectAsState()
    val bodyScroll = rememberScrollState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = JahaizBlack,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = JahaizBlack),
                title = {
                    Text(
                        text = stringResource(id = R.string.app_title_dual),
                        color = JahaizGold,
                    )
                },
            )
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .verticalScroll(bodyScroll),
        ) {
            GlassCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    LottieAnimationView(
                        animationRes = R.raw.money_calculator,
                        modifier = Modifier.fillMaxWidth().height(168.dp),
                    )
                    Text(
                        text = stringResource(id = R.string.hero_tagline),
                        style = MaterialTheme.typography.titleLarge,
                        color = JahaizGold,
                    )
                    Text(
                        text = stringResource(id = R.string.hero_body),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.82f),
                    )
                }
            }

            Row(
                modifier = Modifier.padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top,
            ) {
                GlassCard(modifier = Modifier.weight(1f)) {
                    Text(
                        stringResource(id = R.string.stats_rishta),
                        style = MaterialTheme.typography.bodySmall,
                        color = JahaizGold.copy(alpha = 0.72f),
                    )
                    AnimatedLongCounter(value = analyzed, suffix = "+")
                    Text(
                        text = stringResource(id = R.string.stats_rishta_sub),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f),
                    )
                }
                GlassCard(modifier = Modifier.weight(1f)) {
                    Text(
                        stringResource(id = R.string.stats_rupee_dual),
                        style = MaterialTheme.typography.bodySmall,
                        color = JahaizGold.copy(alpha = 0.72f),
                    )
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(text = "₹ ", color = JahaizGold)
                        AnimatedLongCounter(value = crs)
                        Text(text = "Cr+", color = JahaizGold)
                    }
                    Text(
                        text = stringResource(id = R.string.stats_rupee_sub),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f),
                    )
                }
            }

            Button(
                onClick = onStart,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                colors =
                    ButtonDefaults.buttonColors(containerColor = JahaizAccent.copy(alpha = 0.78f)),
            ) {
                Icon(Icons.Default.Calculate, contentDescription = null, tint = JahaizGold)
                Spacer(Modifier.padding(horizontal = 6.dp))
                Text(text = stringResource(id = R.string.cta_start), color = JahaizGold)
            }

            GlassCard(modifier = Modifier.padding(top = 22.dp).fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.marquee_intro),
                    color = JahaizGold.copy(alpha = 0.78f),
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = stringResource(id = R.string.one_liners_parade),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 10.dp),
                )
            }

            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}
