package com.waseemsgith.jahaiz.core.network

import com.google.gson.JsonObject
import com.waseemsgith.jahaiz.data.model.DowryCalculateBody
import com.waseemsgith.jahaiz.data.model.ImageAnalyzeBody
import com.waseemsgith.jahaiz.data.model.OccupationSatireBody
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("api/image/analyze")
    suspend fun analyzeImage(@Body body: ImageAnalyzeBody): JsonObject

    @POST("api/satire/generate")
    suspend fun generateOccupationSatire(@Body body: OccupationSatireBody): JsonObject

    @POST("api/dowry/calculate")
    suspend fun calculateDowry(@Body body: DowryCalculateBody): JsonObject
}
