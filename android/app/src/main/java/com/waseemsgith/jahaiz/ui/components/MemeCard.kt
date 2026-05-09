package com.waseemsgith.jahaiz.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.waseemsgith.jahaiz.core.ui.theme.JahaizGold

@Composable
fun MemeCardHeadline(text: String, modifier: Modifier = Modifier) {
    GlassCard(modifier = modifier) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black),
            color = JahaizGold,
        )
    }
}
