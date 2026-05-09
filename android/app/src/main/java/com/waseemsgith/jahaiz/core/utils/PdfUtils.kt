package com.waseemsgith.jahaiz.core.utils

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.colors.DeviceRgb
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.properties.TextAlignment
import com.waseemsgith.jahaiz.data.model.FullSatireResult
import com.waseemsgith.jahaiz.data.model.UserInput
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfUtils {

    private val gold = DeviceRgb(255, 215, 0)
    private val footerText = "Built by Waseem Shareef K S | جہیز کا حساب کتاب"

    fun buildShareablePdf(
        context: Context,
        input: UserInput?,
        result: FullSatireResult,
        photoJpeg: ByteArray?,
    ): File {
        val cache = File(context.cacheDir, "jahaiz_exports").apply { mkdirs() }
        val safe = (input?.name ?: "guest").replace(Regex("[^A-Za-z0-9_\\- ]"), "_").replace(" ", "_")
        val ts = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val file = File(cache, "jahaiz_hisaab_${safe}_$ts.pdf")

        val writer = PdfWriter(file)
        val pdf = PdfDocument(writer)
        val doc = Document(pdf)

        doc.add(
            Paragraph("جہیز کا حساب کتاب — JAHAIZ KA HISAAB KITAB")
                .setFontColor(gold)
                .setBold()
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(18f),
        )
        doc.add(
            Paragraph("Awareness satire export (fictional invoice)")
                .setFontColor(ColorConstants.LIGHT_GRAY)
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(10f),
        )
        doc.add(Paragraph("\n"))

        if (photoJpeg != null && photoJpeg.isNotEmpty()) {
            try {
                val img = Image(ImageDataFactory.create(photoJpeg))
                img.scaleToFit(220f, 220f)
                doc.add(img.setTextAlignment(TextAlignment.CENTER))
                doc.add(Paragraph("\n"))
            } catch (_: Exception) {
                // ignore broken image
            }
        }

        doc.add(sectionTitle("AI Image Roast (satire)"))
        doc.add(body(result.image.poseAnalysis))
        doc.add(body(result.image.fashionVibe))
        doc.add(body(result.image.aiRoastLine))
        doc.add(body("Aura tag: ${result.image.detectedAura}"))
        doc.add(body("Premium rishta rating: ${result.image.premiumRishtaRating}"))
        doc.add(body("Tags: ${result.image.floatingTags.joinToString()}"))
        doc.add(body(result.image.memeCaption))

        doc.add(sectionTitle("Occupation Satire"))
        doc.add(body(result.occupation.occupationBadge))
        doc.add(body(result.occupation.dakniUrduRoast))
        doc.add(body(result.occupation.hindiSatire))
        doc.add(body(result.occupation.englishRoast))
        doc.add(body(result.occupation.memeSummary))

        doc.add(sectionTitle(result.dowry.invoiceHeader.ifBlank { "Fake Dowry Invoice" }))
        result.dowry.lineItems.forEach {
            doc.add(
                body("${it.emoji} ${it.item} — ₹${it.amount} — ${it.satireNote}"),
            )
        }
        doc.add(
            Paragraph("TOTAL (fiction): ₹${result.dowry.totalFakeAmount}")
                .setBold()
                .setFontColor(gold)
                .setFontSize(13f),
        )
        doc.add(body(result.dowry.satireDisclaimer))
        doc.add(body(result.dowry.footerJoke))

        doc.add(Paragraph("\n"))
        doc.add(
            Paragraph(footerText)
                .setFontColor(ColorConstants.GRAY)
                .setFontSize(8f)
                .setTextAlignment(TextAlignment.CENTER),
        )

        doc.close()
        return file
    }

    fun sharePdf(context: Context, file: File) {
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file,
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_SUBJECT, "Jahaiz Ka Hisaab Kitab export")
        }
        context.startActivity(Intent.createChooser(intent, "Share PDF"))
    }

    private fun sectionTitle(text: String) =
        Paragraph(text)
            .setBold()
            .setFontColor(gold)
            .setFontSize(12f)

    private fun body(text: String) =
        Paragraph(text.ifBlank { "—" })
            .setFontColor(ColorConstants.LIGHT_GRAY)
            .setFontSize(9f)
}
