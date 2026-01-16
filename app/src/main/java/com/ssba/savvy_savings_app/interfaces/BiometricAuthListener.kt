package com.ssba.savvy_savings_app.interfaces

interface BiometricAuthListener {

    fun onBiometricAuthenticateError(error: Int,errMsg: String)
    fun onBiometricAuthenticateSuccess(result: androidx.biometric.BiometricPrompt.AuthenticationResult)
}