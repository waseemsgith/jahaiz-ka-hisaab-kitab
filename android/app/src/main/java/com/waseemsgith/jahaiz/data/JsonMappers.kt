package com.waseemsgith.jahaiz.data

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.waseemsgith.jahaiz.data.model.DowryInvoice
import com.waseemsgith.jahaiz.data.model.DowryLineItem
import com.waseemsgith.jahaiz.data.model.ImageSatire
import com.waseemsgith.jahaiz.data.model.OccupationSatire

private fun JsonObject.optStr(key: String): String =
    try {
        if (has(key) && !get(key).isJsonNull) get(key).asString else ""
    } catch (_: Exception) {
        ""
    }

private fun JsonObject.optDbl(key: String): Double =
    try {
        when {
            !has(key) || get(key).isJsonNull -> 0.0
            get(key).isJsonPrimitive -> get(key).asDouble
            else -> 0.0
        }
    } catch (_: Exception) {
        0.0
    }

private fun JsonObject.optIntFlexible(key: String): Int =
    try {
        when {
            !has(key) || get(key).isJsonNull -> 0
            get(key).isJsonPrimitive -> {
                val p = get(key).asJsonPrimitive
                when {
                    p.isNumber -> p.asInt
                    else -> p.asString.toDoubleOrNull()?.toInt() ?: p.asString.toIntOrNull() ?: 0
                }
            }
            else -> 0
        }
    } catch (_: Exception) {
        0
    }

private fun parseAmount(el: JsonElement?): Long =
    try {
        when {
            el == null || el.isJsonNull -> 0L
            el.isJsonPrimitive && el.asJsonPrimitive.isNumber -> el.asLong
            el.isJsonPrimitive && el.asJsonPrimitive.isString -> run {
                val raw = el.asString.replace(",", "").trim()
                raw.toDoubleOrNull()?.toLong() ?: raw.toLongOrNull() ?: 0L
            }
            else -> 0L
        }
    } catch (_: Exception) {
        0L
    }

private fun JsonObject.optTags(key: String): List<String> {
    val v = get(key) ?: return emptyList()
    if (!v.isJsonArray) return emptyList()
    val arr = v as JsonArray
    return buildList {
        for (el in arr) {
            try {
                if (el.isJsonPrimitive) add(el.asString)
            } catch (_: Exception) {}
        }
    }
}

fun JsonObject.toImageSatire(): ImageSatire =
    ImageSatire(
        poseAnalysis = optStr("pose_analysis"),
        fashionVibe = optStr("fashion_vibe"),
        detectedAura = optStr("detected_aura"),
        premiumRishtaRating = optDbl("premium_rishta_rating"),
        fakeEgoLevel = optIntFlexible("fake_ego_level"),
        aiRoastLine = optStr("ai_roast_line"),
        floatingTags = optTags("floating_tags"),
        memeCaption = optStr("meme_caption"),
    )

fun JsonObject.toOccupationSatire(): OccupationSatire =
    OccupationSatire(
        dakniUrduRoast = optStr("dakni_urdu_roast"),
        hindiSatire = optStr("hindi_satire"),
        englishRoast = optStr("english_roast"),
        occupationBadge = optStr("occupation_badge"),
        dowryMultiplier = optDbl("dowry_multiplier"),
        egoCommentary = optStr("ego_commentary"),
        familyPressureLine = optStr("family_pressure_line"),
        rishtaPremiumScore = optDbl("rishta_premium_score"),
        memeSummary = optStr("meme_summary"),
    )

fun JsonObject.toDowryInvoice(): DowryInvoice {
    val items = mutableListOf<DowryLineItem>()
    val arr = try {
        getAsJsonArray("line_items")
    } catch (_: Exception) {
        null
    }
    if (arr != null) {
        for (el in arr) {
            if (!el.isJsonObject) continue
            val o = el.asJsonObject
            val amount = parseAmount(o.get("amount"))
            items.add(
                DowryLineItem(
                    item = o.optStr("item"),
                    amount = amount,
                    emoji = o.optStr("emoji"),
                    satireNote = o.optStr("satire_note"),
                ),
            )
        }
    }
    val total = try {
        when {
            has("total_fake_amount") && !get("total_fake_amount").isJsonNull ->
                parseAmount(get("total_fake_amount")).takeIf { it > 0 } ?: items.sumOf { it.amount }
            else -> items.sumOf { it.amount }.takeIf { it > 0 } ?: 0L
        }
    } catch (_: Exception) {
        items.sumOf { it.amount }
    }
    return DowryInvoice(
        totalFakeAmount = total,
        lineItems = items,
        satireDisclaimer = optStr("satire_disclaimer"),
        invoiceHeader = optStr("invoice_header"),
        footerJoke = optStr("footer_joke"),
    )
}
