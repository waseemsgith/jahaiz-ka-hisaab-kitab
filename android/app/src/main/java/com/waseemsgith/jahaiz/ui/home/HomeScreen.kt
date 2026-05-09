package com.waseemsgith.jahaiz.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.waseemsgith.jahaiz.R
import com.waseemsgith.jahaiz.core.ui.theme.JahaizAccent
import com.waseemsgith.jahaiz.core.ui.theme.JahaizBlack
import com.waseemsgith.jahaiz.core.ui.theme.JahaizGold
import com.waseemsgith.jahaiz.core.ui.theme.JahaizGoldDark
import com.waseemsgith.jahaiz.ui.components.AnimatedLongCounter
import com.waseemsgith.jahaiz.ui.components.GlassCard
import com.waseemsgith.jahaiz.ui.components.LottieAnimationView
import kotlinx.coroutines.delay

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

    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(100)
        isVisible = true
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = JahaizBlack,
    ) { padding ->
        // Background Gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF151515),
                            JahaizBlack,
                            Color(0xFF0F0F0F)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(bodyScroll),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                // Hero Section
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(800)) + slideInVertically(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 3 }
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        LottieAnimationView(
                            animationRes = R.raw.money_calculator,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                        )
                        Text(
                            text = "JAHAIZ KA HISAAB",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp
                            ),
                            color = JahaizGold,
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Text(
                            text = "Rishte Ka Rate Card 📋 | Kyunki Dowry is Too Mainstream",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Main CTA
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(800, delayMillis = 200)) + slideInVertically(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 3 }
                ) {
                    GlowingButton(onClick = onStart)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Quick Features
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(800, delayMillis = 400)) + slideInVertically(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 3 }
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "FEATURES",
                            color = JahaizGoldDark,
                            style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.5.sp),
                            modifier = Modifier.padding(bottom = 12.dp, start = 8.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FeatureCard(
                                icon = Icons.Default.SmartToy,
                                title = "AI Satire",
                                modifier = Modifier.weight(1f)
                            )
                            FeatureCard(
                                icon = Icons.Default.Calculate,
                                title = "Calculator",
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            FeatureCard(
                                icon = Icons.Default.PhotoCamera,
                                title = "Photo Aura",
                                modifier = Modifier.weight(1f)
                            )
                            FeatureCard(
                                icon = Icons.Default.PictureAsPdf,
                                title = "PDF Export",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Stats Section
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(800, delayMillis = 600)) + slideInVertically(spring(dampingRatio = Spring.DampingRatioMediumBouncy)) { it / 3 }
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "LIVE SATIRE METRICS",
                            color = JahaizGoldDark,
                            style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.5.sp),
                            modifier = Modifier.padding(bottom = 12.dp, start = 8.dp)
                        )
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            StatsCard(
                                title = "Egos Roasted",
                                valueContent = {
                                    AnimatedLongCounter(value = analyzed, suffix = "+", style = MaterialTheme.typography.headlineMedium.copy(color = JahaizGold, fontWeight = FontWeight.Bold))
                                },
                                subtitle = "Delusions shattered",
                                modifier = Modifier.weight(1f)
                            )
                            StatsCard(
                                title = "Fictional Demands",
                                valueContent = {
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(text = "₹ ", color = JahaizGold, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
                                        AnimatedLongCounter(value = crs, style = MaterialTheme.typography.headlineMedium.copy(color = JahaizGold, fontWeight = FontWeight.Bold))
                                        Text(text = "Cr+", color = JahaizGold, style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold))
                                    }
                                },
                                subtitle = "In meme economy",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Marquee
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(800, delayMillis = 800))
                ) {
                    SatireMarquee()
                }

                Spacer(modifier = Modifier.height(48.dp))

                // Elegant Footer
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(tween(1000, delayMillis = 1000))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(bottom = 32.dp)
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.width(60.dp).padding(bottom = 16.dp),
                            color = JahaizGoldDark.copy(alpha = 0.3f),
                            thickness = 2.dp
                        )
                        Text(
                            text = "Built by Waseem Shareef K S",
                            color = Color.White.copy(alpha = 0.5f),
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            text = "جہیز کا حساب کتاب",
                            color = JahaizGold.copy(alpha = 0.4f),
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GlowingButton(onClick: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowAlpha"
    )
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "buttonScale"
    )

    Box(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        contentAlignment = Alignment.Center
    ) {
        // Glow effect
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(64.dp)
                .drawBehind {
                    drawRoundRect(
                        color = JahaizAccent.copy(alpha = glowAlpha),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(32.dp.toPx())
                    )
                }
                .shadow(
                    elevation = 20.dp,
                    shape = RoundedCornerShape(32.dp),
                    spotColor = JahaizAccent,
                    ambientColor = JahaizAccent
                )
        )

        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = JahaizAccent,
                contentColor = JahaizBlack
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp, pressedElevation = 4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(Icons.Default.FlashOn, contentDescription = null, tint = JahaizBlack)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "START HISAAB",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                )
            }
        }
    }
}

@Composable
fun FeatureCard(icon: ImageVector, title: String, modifier: Modifier = Modifier) {
    GlassCard(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(JahaizGold.copy(alpha = 0.1f), CircleShape)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = JahaizGold, modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = Color.White
            )
        }
    }
}

@Composable
fun StatsCard(title: String, valueContent: @Composable () -> Unit, subtitle: String, modifier: Modifier = Modifier) {
    GlassCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(vertical = 4.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = JahaizGoldDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            valueContent()
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
fun SatireMarquee() {
    val items = listOf(
        "IAS package detected 💰",
        "NRI demand increasing 📈",
        "Fortuner expectation overloaded 🚗",
        "Ego level: Over 9000 🧨",
        "Dowry is a crime ⚖️",
        "Gold price checking in... 💍"
    )

    // A simple scrolling marquee effect
    val infiniteTransition = rememberInfiniteTransition(label = "marquee")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "marqueeOffset"
    )

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = JahaizGold, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Box(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.graphicsLayer { translationX = offset }
                ) {
                    // Repeat items to simulate infinite scroll visually
                    repeat(3) {
                        items.forEach { item ->
                            Text(
                                text = item,
                                color = Color.White.copy(alpha = 0.8f),
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Text("•", color = JahaizGoldDark)
                        }
                    }
                }
            }
        }
    }
}
