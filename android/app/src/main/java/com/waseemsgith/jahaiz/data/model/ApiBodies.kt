package com.waseemsgith.jahaiz.data.model

import com.google.gson.annotations.SerializedName

data class ImageAnalyzeBody(
    @SerializedName("image_base64") val imageBase64: String,
    @SerializedName("mime_type") val mimeType: String = "image/jpeg",
)

data class OccupationSatireBody(
    val occupation: String,
    val salary: Int,
    @SerializedName("ego_level") val egoLevel: Int,
    @SerializedName("abroad_status") val abroadStatus: String,
)

data class DowryCalculateBody(
    val occupation: String,
    val salary: Int,
    @SerializedName("abroad_status") val abroadStatus: String,
    @SerializedName("ego_level") val egoLevel: Int,
    @SerializedName("family_expectation") val familyExpectation: Int,
    @SerializedName("luxury_level") val luxuryLevel: Int,
    @SerializedName("gold_kg") val goldKg: Double,
    val car: String,
    @SerializedName("wedding_level") val weddingLevel: String,
    @SerializedName("property_sqft") val propertySqft: Int? = null,
)
