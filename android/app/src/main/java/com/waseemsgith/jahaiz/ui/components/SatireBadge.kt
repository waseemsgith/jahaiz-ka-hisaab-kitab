package com.waseemsgith.jahaiz.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.waseemsgith.jahaiz.core.ui.theme.JahaizBlack
import com.waseemsgith.jahaiz.core.ui.theme.JahaizGold
import com.waseemsgith.jahaiz.core.ui.theme.JahaizGlassBorder

@Composable
fun SatireBadge(text: String, modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier
                .border(1.dp, JahaizGlassBorder, CircleShape)
                .background(JahaizBlack.copy(alpha = 0.7f), CircleShape)
                .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        Text(
            text = text,
            color = JahaizGold,
            style = androidx.compose.material3.MaterialTheme.typography.labelLarge,
            textAlign = TextAlign.Center,
        )
    }
}
