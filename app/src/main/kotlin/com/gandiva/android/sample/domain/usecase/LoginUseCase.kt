package com.gandiva.android.sample.domain.usecase

import com.gandiva.android.sample.domain.model.Email
import com.gandiva.android.sample.domain.model.Password
import kotlinx.coroutines.delay
import javax.inject.Inject

interface LoginUseCase {
    suspend fun login(email: Email, password: Password)
}

class LoginUseCaseImpl @Inject constructor() : LoginUseCase {
    override suspend fun login(email: Email, password: Password) {
        // Assume we are doing login
        delay(1500L)
        // Assume login done
    }
}