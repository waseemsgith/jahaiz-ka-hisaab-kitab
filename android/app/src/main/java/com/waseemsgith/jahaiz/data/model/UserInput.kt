package com.waseemsgith.jahaiz.data.model

import android.net.Uri

data class UserInput(
    val name: String,
    val photoUri: Uri?,
    val occupationKey: String,
    val salaryMonthly: Int,
    val abroadStatus: String,
    val egoLevel: Int,
    val familyExpectation: Int,
    val luxuryDemand: Int,
    val goldKg: Double,
    val car: String,
    val propertySqft: Int,
    val weddingLevel: String,
)
