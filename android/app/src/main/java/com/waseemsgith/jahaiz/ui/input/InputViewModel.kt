package com.waseemsgith.jahaiz.ui.input

import androidx.lifecycle.ViewModel
import com.waseemsgith.jahaiz.data.model.UserInput
import com.waseemsgith.jahaiz.data.repository.AppSession
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InputViewModel @Inject constructor(
    private val session: AppSession,
) : ViewModel() {
    fun saveDraft(input: UserInput) {
        session.draftInput = input
    }
}
