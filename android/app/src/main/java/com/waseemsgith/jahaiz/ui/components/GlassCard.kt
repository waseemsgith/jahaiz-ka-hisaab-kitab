package com.waseemsgith.jahaiz.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.waseemsgith.jahaiz.core.ui.theme.JahaizDarkCard
import com.waseemsgith.jahaiz.core.ui.theme.JahaizGlass
import com.waseemsgith.jahaiz.core.ui.theme.JahaizGlassBorder

@Composable
fun GlassCard(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .background(JahaizGlass, shape)
                .border(1.dp, JahaizGlassBorder, shape)
                .background(
                    Brush.verticalGradient(
                        listOf(JahaizDarkCard.copy(alpha = 0.92f), JahaizDarkCard.copy(alpha = 0.75f)),
                    ),
                    shape,
                )
                .padding(16.dp),
    ) {
        content()
    }
}
