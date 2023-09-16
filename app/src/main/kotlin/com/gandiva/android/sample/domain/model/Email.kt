package com.gandiva.android.sample.domain.model

import android.os.Parcelable
import androidx.core.util.PatternsCompat
import kotlinx.parcelize.Parcelize

@Parcelize
data class Email(val value: String?) : Parcelable {
    fun isValid(): Boolean {
        return if (value == null) false else PatternsCompat.EMAIL_ADDRESS.matcher(value).matches()
    }
}
