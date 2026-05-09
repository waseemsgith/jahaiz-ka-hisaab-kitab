package com.waseemsgith.jahaiz.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import kotlin.math.max
import kotlin.math.min

object ImageUtils {

    /**
     * Reads an image [Uri] and returns JPEG base64 + mime for Gemini.
     */
    suspend fun jpegBase64FromUri(context: Context, uri: Uri): Pair<String, String>? =
        withContext(Dispatchers.IO) {
            try {
                val bytes = jpegBytes(context, uri) ?: return@withContext null
                val b64 = android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
                Pair(b64, "image/jpeg")
            } catch (_: Exception) {
                null
            }
        }

    suspend fun jpegBytesFromUri(context: Context, uri: Uri): ByteArray? =
        withContext(Dispatchers.IO) {
            jpegBytes(context, uri)
        }

    private fun jpegBytes(context: Context, uri: Uri): ByteArray? {
        val resolver = context.contentResolver
        val bmp = resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it) } ?: return null
        val scaled = scaleBitmap(bmp, 1024)

        fun compress(q: Int) = ByteArrayOutputStream().use { bos ->
            scaled.compress(Bitmap.CompressFormat.JPEG, q, bos)
            bos.toByteArray()
        }

        var q = 90
        var out = compress(q)
        while (out.size > 1_200_000 && q > 40) {
            q -= 10
            out = compress(q)
        }
        return out
    }

    private fun scaleBitmap(src: Bitmap, maxSide: Int): Bitmap {
        val w = src.width
        val h = src.height
        val longest = max(w, h)
        if (longest <= maxSide) return src
        val scale = maxSide.toFloat() / longest.toFloat()
        val nw = max(1, (w * scale).toInt())
        val nh = max(1, (h * scale).toInt())
        return Bitmap.createScaledBitmap(src, nw, nh, true)
    }
}
