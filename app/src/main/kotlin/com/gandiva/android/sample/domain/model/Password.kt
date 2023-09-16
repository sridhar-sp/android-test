package com.gandiva.android.sample.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Password(val value: String?) : Parcelable {
    fun isValid(): Boolean {
        return if (value == null) false else value.length > 3
    }
}
