package com.waseemsgith.jahaiz.data.repository

import com.waseemsgith.jahaiz.core.network.ApiService
import com.waseemsgith.jahaiz.data.model.DowryCalculateBody
import com.waseemsgith.jahaiz.data.model.FullSatireResult
import com.waseemsgith.jahaiz.data.model.ImageAnalyzeBody
import com.waseemsgith.jahaiz.data.model.ImageSatire
import com.waseemsgith.jahaiz.data.model.OccupationSatireBody
import com.waseemsgith.jahaiz.data.model.OccupationType
import com.waseemsgith.jahaiz.data.model.UserInput
import com.waseemsgith.jahaiz.data.toDowryInvoice
import com.waseemsgith.jahaiz.data.toImageSatire
import com.waseemsgith.jahaiz.data.toOccupationSatire
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class JahaizRepository @Inject constructor(
    private val api: ApiService,
) {
    suspend fun analyzeAll(input: UserInput, imageBase64: String?, mime: String?): Result<FullSatireResult> =
        withContext(Dispatchers.IO) {
            try {
                val occ = OccupationType.fromKey(input.occupationKey)

                coroutineScope {
                    val occupationJob = async {
                        api.generateOccupationSatire(
                            OccupationSatireBody(
                                occupation = occ.displayName,
                                salary = input.salaryMonthly,
                                egoLevel = input.egoLevel,
                                abroadStatus = input.abroadStatus,
                            ),
                        ).toOccupationSatire()
                    }

                    val imageJob = async {
                        val b64 = imageBase64
                        if (!b64.isNullOrBlank()) {
                            api.analyzeImage(ImageAnalyzeBody(b64.trim(), mime ?: "image/jpeg")).toImageSatire()
                        } else {
                            placeholderImageSatire(input.name)
                        }
                    }

                    val dowryJob = async {
                        api.calculateDowry(dowryBody(input, occ.displayName)).toDowryInvoice()
                    }

                    Result.success(FullSatireResult(image = imageJob.await(), occupation = occupationJob.await(), dowry = dowryJob.await()))
                }
            } catch (_: Throwable) {
                Result.failure(RuntimeException("Network / parsing error"))
            }
        }

    private fun dowryBody(input: UserInput, occupationLabel: String) =
        DowryCalculateBody(
            occupation = occupationLabel,
            salary = input.salaryMonthly,
            abroadStatus = input.abroadStatus,
            egoLevel = input.egoLevel,
            familyExpectation = input.familyExpectation,
            luxuryLevel = input.luxuryDemand,
            goldKg = input.goldKg,
            car = input.car,
            weddingLevel = input.weddingLevel,
            propertySqft = input.propertySqft.takeIf { it > 0 },
        )

    private fun placeholderImageSatire(name: String) =
        ImageSatire(
            poseAnalysis = "${name.ifBlank { "Guest" }} ke liye photo skip — aura auto satire enabled.",
            fashionVibe = "No-fit data, full fiction — target is dowry myths, not faces.",
            detectedAura = "manual_mode",
            premiumRishtaRating = 7.4,
            fakeEgoLevel = 6,
            aiRoastLine = "Photo nahi, phir bhi rate card ka template open ho gaya (satire).",
            floatingTags = listOf("Camera Shy Premium", "LinkedIn Energy (meme)", "Invoice Mode ON"),
            memeCaption = "Awareness app bolega: dowry demand nahi, humour target hai.",
        )
}
