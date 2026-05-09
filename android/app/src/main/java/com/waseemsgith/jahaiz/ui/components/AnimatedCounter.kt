package com.waseemsgith.jahaiz.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.waseemsgith.jahaiz.core.ui.theme.JahaizGold

@Composable
fun AnimatedLongCounter(
    value: Long,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.titleLarge,
    prefix: String = "",
    suffix: String = "",
) {
    val anim = remember { Animatable(0f) }
    LaunchedEffect(value) {
        anim.snapTo(0f)
        anim.animateTo(value.toFloat(), animationSpec = tween(900))
    }
    Text(
        text = "$prefix${anim.value.toLong()}$suffix",
        modifier = modifier,
        style = style.copy(color = JahaizGold),
    )
}
