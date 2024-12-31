package com.gandiva.android.sample.domain.usecase

import com.gandiva.android.sample.domain.model.Email
import com.gandiva.android.sample.domain.model.Password
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.delay
import javax.inject.Inject

interface LoginUseCase {
    suspend fun login(email: Email, password: Password)
}

class LoginUseCaseImpl @Inject constructor() : LoginUseCase {
    override suspend fun login(email: Email, password: Password) {
        // Assume we are doing login
        println("***** Begin of IO context")
//        withContext(Dispatchers.IO) { // Don't do this, you can't write unit test, pass it as constructor param
//            println("**** Start Delay in IO")
//            val startTime = System.currentTimeMillis()
//            delay(50L)
//            println("**** End Delay in IO. Took ${System.currentTimeMillis() - startTime} mill seconds")
//        }
        println("***** End of IO context")
        val startTime = System.currentTimeMillis()
        delay(1500L)
        println("***** Return from login method Took ${System.currentTimeMillis() - startTime} mill seconds")
        // Assume login done
    }
}

@Module
@InstallIn(ViewModelComponent::class)
interface LoginModule {

    @Binds
    fun bindLoginUseCase(loginUseCaseImpl: LoginUseCaseImpl): LoginUseCase
}