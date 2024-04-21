package com.gandiva.android.sample.presentation.main

import com.gandiva.android.sample.domain.model.Email
import com.gandiva.android.sample.domain.model.Password
import com.gandiva.android.sample.domain.usecase.LoginUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MainScreenTestModule {

    @Provides
    fun provideLoginUseCase(): LoginUseCase {
        return object : LoginUseCase {
            override suspend fun login(email: Email, password: Password) {
                println("****** login call in LoginUseCase")
            }

            override fun toString(): String {
                return "Login use case created form MainScreenTestModule"
            }
        }
    }

}