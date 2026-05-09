package com.waseemsgith.jahaiz.data.model

data class ImageSatire(
    val poseAnalysis: String,
    val fashionVibe: String,
    val detectedAura: String,
    val premiumRishtaRating: Double,
    val fakeEgoLevel: Int,
    val aiRoastLine: String,
    val floatingTags: List<String>,
    val memeCaption: String,
)

data class OccupationSatire(
    val dakniUrduRoast: String,
    val hindiSatire: String,
    val englishRoast: String,
    val occupationBadge: String,
    val dowryMultiplier: Double,
    val egoCommentary: String,
    val familyPressureLine: String,
    val rishtaPremiumScore: Double,
    val memeSummary: String,
)

data class DowryLineItem(
    val item: String,
    val amount: Long,
    val emoji: String,
    val satireNote: String,
)

data class DowryInvoice(
    val totalFakeAmount: Long,
    val lineItems: List<DowryLineItem>,
    val satireDisclaimer: String,
    val invoiceHeader: String,
    val footerJoke: String,
)

data class FullSatireResult(
    val image: ImageSatire,
    val occupation: OccupationSatire,
    val dowry: DowryInvoice,
)
