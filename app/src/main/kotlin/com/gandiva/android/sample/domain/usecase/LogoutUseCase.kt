package com.gandiva.android.sample.domain.usecase

import com.gandiva.android.sample.domain.model.Email
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.delay
import javax.inject.Inject

interface LogoutUseCase {

    suspend fun logout(email: Email)

}

class LogoutUseCaseImpl @Inject constructor() : LogoutUseCase {
    override suspend fun logout(email: Email) {
        // Assume we do logout operation
        delay(1500L)
        // Logout operation success
    }
}

@Module
@InstallIn(ViewModelComponent::class)
interface LogoutModule {

    @Binds
    fun bindLoginUseCase(logoutUseCaseImpl: LogoutUseCaseImpl): LogoutUseCase
}