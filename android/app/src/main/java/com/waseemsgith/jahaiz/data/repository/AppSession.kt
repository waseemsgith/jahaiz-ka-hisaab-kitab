package com.waseemsgith.jahaiz.data.repository

import com.waseemsgith.jahaiz.data.model.FullSatireResult
import com.waseemsgith.jahaiz.data.model.UserInput
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppSession @Inject constructor() {
    @Volatile var draftInput: UserInput? = null

    @Volatile var lastInput: UserInput? = null

    @Volatile var lastResult: FullSatireResult? = null

    /** JPEG bytes captured for PDF export/share. */
    @Volatile var lastPhotoJpeg: ByteArray? = null

    fun clearPresentation() {
        lastResult = null
        lastInput = null
        lastPhotoJpeg = null
    }
}
