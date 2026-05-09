package com.waseemsgith.jahaiz.ui.result

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.waseemsgith.jahaiz.R
import com.waseemsgith.jahaiz.core.ui.theme.JahaizAccent
import com.waseemsgith.jahaiz.core.ui.theme.JahaizBlack
import com.waseemsgith.jahaiz.core.ui.theme.JahaizGold
import com.waseemsgith.jahaiz.data.model.FullSatireResult
import com.waseemsgith.jahaiz.data.model.UserInput
import com.waseemsgith.jahaiz.ui.components.AnimatedLongCounter
import com.waseemsgith.jahaiz.ui.components.GlassCard
import com.waseemsgith.jahaiz.ui.components.MemeCardHeadline
import com.waseemsgith.jahaiz.ui.components.SatireBadge
import com.waseemsgith.jahaiz.core.utils.PdfUtils
import com.waseemsgith.jahaiz.ui.components.LottieAnimationView
import kotlinx.coroutines.delay

@Composable
fun ResultScreen(
    onRestart: () -> Unit,
    onBackHome: () -> Unit,
    modifier: Modifier = Modifier,
    vm: ResultViewModel = hiltViewModel(),
) {
    val input = vm.session.lastInput
    val result = vm.session.lastResult
    val scroll = rememberScrollState()
    val context = LocalContext.current

    if (input == null || result == null) {
        LaunchedEffect(Unit) { onBackHome() }
        return
    }

    Scaffold(containerColor = JahaizBlack) { pad ->
        Column(
            Modifier
                .padding(pad)
                .fillMaxSize()
                .verticalScroll(scroll)
                .padding(horizontal = 14.dp, vertical = 12.dp),
        ) {
            LottieAnimationView(
                animationRes = R.raw.result_reveal,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                iterations = 1,
                speed = 1f,
            )

            ImageRoastCard(input, result)

            OccupationCard(result)

            InvoiceCard(result)

            ScoreRow(input, result)

            MemeCardHeadline(text = result.occupation.memeSummary)

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        val file = PdfUtils.buildShareablePdf(context, input, result, vm.session.lastPhotoJpeg)
                        PdfUtils.sharePdf(context, file)
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = JahaizAccent.copy(alpha = 0.72f)),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = JahaizGold)
                        Spacer(Modifier.width(6.dp))
                        Text(stringResource(id = R.string.pdf_dual), color = JahaizGold)
                    }
                }
                Button(
                    onClick = { shareText(context, buildShareText(input, result)) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = JahaizAccent.copy(alpha = 0.52f)),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = JahaizGold)
                        Spacer(Modifier.width(6.dp))
                        Text(stringResource(id = R.string.share_dual), color = JahaizGold)
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
                Button(
                    onClick = {
                        vm.clearForRetry()
                        onRestart()
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = JahaizAccent.copy(alpha = 0.35f)),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Refresh, contentDescription = null, tint = JahaizGold)
                        Spacer(Modifier.width(6.dp))
                        Text(stringResource(id = R.string.retry_dual), color = JahaizGold)
                    }
                }
                Button(
                    onClick = onBackHome,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = JahaizAccent.copy(alpha = 0.25f)),
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Home, contentDescription = null, tint = JahaizGold)
                        Spacer(Modifier.width(6.dp))
                        Text(stringResource(id = R.string.home_dual), color = JahaizGold)
                    }
                }
            }

            Text(
                text = stringResource(id = R.string.footer_credit_dual),
                style = MaterialTheme.typography.bodySmall,
                color = JahaizGold.copy(alpha = 0.45f),
                modifier = Modifier.padding(top = 18.dp),
            )

            Spacer(Modifier.height(80.dp))
        }
    }
}

@Composable
private fun ImageRoastCard(input: UserInput, result: FullSatireResult) {
    GlassCard(modifier = Modifier.padding(bottom = 10.dp)) {
        Column {
            Text(stringResource(id = R.string.section_image_roast_dual), color = JahaizGold, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(10.dp))
            input.photoUri?.let { uri ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                )
                Spacer(Modifier.height(10.dp))
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    result.image.floatingTags.take(6).forEach { tag ->
                        SatireBadge(text = tag)
                    }
                }
            }
            Text(
                text =
                    stringResource(
                        id = R.string.premium_rating_dual,
                        result.image.premiumRishtaRating,
                    ),
                color = JahaizGold.copy(alpha = 0.92f),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp),
            )
            Text(text = result.image.poseAnalysis, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f))
            Text(text = result.image.aiRoastLine, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f))
        }
    }
}

@Composable
private fun OccupationCard(result: FullSatireResult) {
    GlassCard(modifier = Modifier.padding(bottom = 10.dp)) {
        Column {
            Text(stringResource(id = R.string.section_occ_dual), color = JahaizGold, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            SatireBadge(text = result.occupation.occupationBadge)
            Spacer(Modifier.height(12.dp))

            DakniReveal(text = result.occupation.dakniUrduRoast)

            Text(text = result.occupation.hindiSatire, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.84f))
            Text(text = result.occupation.englishRoast, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f))

            Text(
                text =
                    "${result.occupation.egoCommentary}\n${result.occupation.familyPressureLine}",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.76f),
                modifier = Modifier.padding(top = 8.dp),
            )
        }
    }
}

@Composable
private fun InvoiceCard(result: FullSatireResult) {
    GlassCard(modifier = Modifier.padding(bottom = 10.dp)) {
        Column {
            Text(
                text = result.dowry.invoiceHeader,
                style = MaterialTheme.typography.titleLarge,
                color = JahaizGold,
            )
            result.dowry.lineItems.forEach { line ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("${line.emoji} ${line.item}", Modifier.weight(1f), color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.88f))
                    AnimatedLongCounter(value = line.amount, prefix = "₹", suffix = "")
                }
                Text(line.satireNote, style = MaterialTheme.typography.bodySmall, color = JahaizGold.copy(alpha = 0.55f))
            }
            Text(
                stringResource(id = R.string.total_jahaiz_dual),
                Modifier.padding(top = 10.dp),
                color = JahaizGold,
                fontWeight = FontWeight.Bold,
            )
            AnimatedLongCounter(value = result.dowry.totalFakeAmount, prefix = "₹")
            Text(result.dowry.satireDisclaimer, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.74f))
            Text(result.dowry.footerJoke, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f))
        }
    }
}

@Composable
private fun ScoreRow(
    input: UserInput,
    result: FullSatireResult,
) {
    GlassCard(modifier = Modifier.padding(bottom = 10.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(stringResource(id = R.string.scorecard_dual), color = JahaizGold, style = MaterialTheme.typography.titleMedium)

            GaugeLine(stringResource(id = R.string.label_ego_dual), input.egoLevel / 10f)
            GaugeLine(stringResource(id = R.string.label_family_dual), input.familyExpectation / 10f)
            GaugeLine(stringResource(id = R.string.label_luxury_dual), input.luxuryDemand / 10f)

            Text(
                "${stringResource(id = R.string.rishta_ai_dual)}: ${result.occupation.rishtaPremiumScore} | ${stringResource(id = R.string.dow_mult_dual)} ${result.occupation.dowryMultiplier}",
                color = JahaizGold.copy(alpha = 0.65f),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun GaugeLine(
    title: String,
    fraction: Float,
) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(title, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f))
            Text("${(fraction * 100f).toInt()}%", color = JahaizGold.copy(alpha = 0.74f))
        }
        LinearProgressIndicator(
            progress = { fraction },
            modifier =
                Modifier
                    .fillMaxWidth(),
            trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
            color = JahaizGold,
        )
    }
}

@Composable
private fun DakniReveal(text: String) {
    var shown by remember { mutableIntStateOf(0) }
    LaunchedEffect(text) {
        shown = 0
        while (shown < text.length) {
            delay(18)
            shown++
        }
    }
    Text(
        text = text.take(shown.coerceAtMost(text.length)),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.92f),
        fontWeight = FontWeight.Medium,
    )
}

private fun buildShareText(
    input: UserInput,
    result: FullSatireResult,
): String =
    buildString {
        appendLine("${input.name.ifBlank { "Guest" }} — Jahaiz Ka Hisaab Kitab / جہیز کا حساب کتاب")
        appendLine(result.occupation.memeSummary)
        appendLine("Total fictional invoice: ₹${result.dowry.totalFakeAmount}")
        appendLine("Built by Waseem Shareef K S")
    }.trim()

private fun shareText(
    ctx: android.content.Context,
    text: String,
) {
    val send =
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
    ctx.startActivity(Intent.createChooser(send, "Share satire"))
}
