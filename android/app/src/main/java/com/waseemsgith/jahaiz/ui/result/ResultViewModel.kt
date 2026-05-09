package com.waseemsgith.jahaiz.ui.result

import androidx.lifecycle.ViewModel
import com.waseemsgith.jahaiz.data.repository.AppSession
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    val session: AppSession,
) : ViewModel() {
    fun clearForRetry() {
        session.lastResult = null
        session.lastInput = null
        session.lastPhotoJpeg = null
        session.draftInput = null
    }
}
