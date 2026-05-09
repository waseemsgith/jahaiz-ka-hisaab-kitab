package com.waseemsgith.jahaiz.ui.processing

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waseemsgith.jahaiz.core.utils.ImageUtils
import com.waseemsgith.jahaiz.data.repository.AppSession
import com.waseemsgith.jahaiz.data.repository.JahaizRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@HiltViewModel
class ProcessingViewModel @Inject constructor(
    private val repo: JahaizRepository,
    private val session: AppSession,
    @ApplicationContext private val appContext: Context,
) : ViewModel() {
    private val _stepIndex = MutableStateFlow(0)
    val stepIndex: StateFlow<Int> = _stepIndex.asStateFlow()

    fun start(done: () -> Unit, failed: () -> Unit) {
        viewModelScope.launch {
            val input =
                session.draftInput ?: run {
                    failed()
                    return@launch
                }
            _stepIndex.value = 0

            val photoBytesDeferred = async { input.photoUri?.let { uri -> ImageUtils.jpegBytesFromUri(appContext, uri) } }
            val base64Deferred = async { input.photoUri?.let { uri -> ImageUtils.jpegBase64FromUri(appContext, uri) } }

            session.lastPhotoJpeg = photoBytesDeferred.await()
            val pair = base64Deferred.await()

            val analysisJob = async { repo.analyzeAll(input, pair?.first, pair?.second) }

            val ticker =
                launch {
                    while (isActive) {
                        delay(820)
                        if (_stepIndex.value < 5) {
                            _stepIndex.value = _stepIndex.value + 1
                        }
                    }
                }

            val result = analysisJob.await()
            ticker.cancel()
            ticker.join()
            _stepIndex.value = 5

            result.fold(
                onSuccess = {
                    session.lastInput = input
                    session.lastResult = it
                    session.draftInput = null
                    done()
                },
                onFailure = {
                    failed()
                },
            )
        }
    }
}
