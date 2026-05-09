package com.waseemsgith.jahaiz.ui.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _analyzedCount = MutableStateFlow(10_042L)
    val analyzedCount: StateFlow<Long> = _analyzedCount.asStateFlow()

    private val _rupeeCr = MutableStateFlow(50L)
    val rupeeCr: StateFlow<Long> = _rupeeCr.asStateFlow()
}
