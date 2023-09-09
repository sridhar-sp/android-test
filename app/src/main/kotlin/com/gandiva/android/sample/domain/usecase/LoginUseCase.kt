package com.gandiva.android.sample.domain.usecase

import kotlinx.coroutines.delay
import javax.inject.Inject

interface LoginUseCase {
    suspend fun login(email: String, password: String)
}

class LoginUseCaseImpl @Inject constructor() : LoginUseCase {
    override suspend fun login(email: String, password: String) {
        // Assume we are doing login
        delay(1500L)
        // Assume login done
    }
}