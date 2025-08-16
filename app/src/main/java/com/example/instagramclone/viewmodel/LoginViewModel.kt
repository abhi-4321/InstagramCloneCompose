package com.example.instagramclone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.model.LoginRequest
import com.example.instagramclone.model.OtpRequest
import com.example.instagramclone.model.RegistrationRequest
import com.example.instagramclone.model.TokenValidationResponse
import com.example.instagramclone.network.login.RetrofitInterfaceLogin
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.internal.EMPTY_RESPONSE
import retrofit2.Response

class LoginViewModel(private val retrofitInterfaceLogin: RetrofitInterfaceLogin) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginState>(LoginState.Idle)
    val uiState: StateFlow<LoginState> = _uiState // Expose state to UI

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            _uiState.emit(LoginState.Loading) // Show loading UI
            try {
                val response = retrofitInterfaceLogin.login(loginRequest)
                if (response.isSuccessful && response.body() != null) {
                    _uiState.emit(LoginState.Success(token = response.body()!!.token))
                } else {
                    _uiState.emit(LoginState.Error("Invalid credentials"))
                }
            } catch (_: Exception) {
                _uiState.emit(LoginState.Error("Network error"))
            }
        }
    }

    sealed class LoginState {
        data object Idle : LoginState()
        data object Loading : LoginState()
        data class Success(val token: String) : LoginState()
        data class Error(val message: String) : LoginState()
    }

    private val _uiStateReg = MutableStateFlow<LoginState>(LoginState.Idle)
    val uiStateReg: StateFlow<LoginState> = _uiStateReg // Expose state to UI

    fun register() {
        viewModelScope.launch {
            _uiStateReg.emit(LoginState.Loading) // Show loading UI
            try {
                val registrationRequest = RegistrationRequest(
                    registrationDetails.email!!,
                    registrationDetails.username!!,
                    registrationDetails.password!!,
                    registrationDetails.fullName!!
                )
                val response = retrofitInterfaceLogin.register(registrationRequest)
                if (response.isSuccessful && response.body() != null) {
                    _uiStateReg.emit(LoginState.Success(token = response.body()!!.token))
                } else {
                    _uiStateReg.emit(LoginState.Error("Invalid credentials"))
                }
            } catch (e: Exception) {
                _uiStateReg.emit(LoginState.Error("Network error"))
            }
        }
    }

    sealed class UsernameState {
        data object Empty : UsernameState()
        data object Validating : UsernameState()
        data object Valid : UsernameState()
        data object Invalid : UsernameState()
        data object Error : UsernameState()
    }

    private val _usernameState = MutableStateFlow<UsernameState>(UsernameState.Empty)
    val usernameState: StateFlow<UsernameState> = _usernameState // Expose state to UI

    fun validateUsername(username: String) {
        viewModelScope.launch {
            _usernameState.emit(UsernameState.Validating) // Show loading UI
            try {
                val response = retrofitInterfaceLogin.validateUsername(username)
                if (response.code() == 200) {
                    _usernameState.value = UsernameState.Valid
                } else if (response.code() == 409) {
                    _usernameState.value = UsernameState.Invalid
                } else {
                    _usernameState.value = UsernameState.Error
                }
            } catch (e: Exception) {
                _usernameState.value = UsernameState.Error
            }
        }
    }

    sealed class OtpRequestState {
        data object Idle : OtpRequestState()
        data object Loading : OtpRequestState()
        data object Sent : OtpRequestState()
        data object Exists : OtpRequestState()
        data object Error : OtpRequestState()
    }

    private val _otpState = MutableStateFlow<OtpRequestState>(OtpRequestState.Idle)
    val otpState: StateFlow<OtpRequestState> = _otpState // Expose state to UI

    fun sendOtp(email: String = registrationDetails.email!!) {
        viewModelScope.launch {
            _otpState.emit(OtpRequestState.Loading) // Show loading UI
            try {
                val response = retrofitInterfaceLogin.sendOtp(OtpRequest(email, ""))
                if (response.isSuccessful && response.body() != null && response.body()!!.success) {
                    _otpState.emit(OtpRequestState.Sent)
                    delay(100)
                    _otpState.emit(OtpRequestState.Idle)
                } else if (response.code() == 401) {
                    _otpState.emit(OtpRequestState.Exists)
                } else {
                    _otpState.emit(OtpRequestState.Error)
                }
            } catch (e: Exception) {
                _otpState.emit(OtpRequestState.Error)
            }
        }
    }

    sealed class OtpVerifyState {
        data object Idle : OtpVerifyState()
        data object Loading : OtpVerifyState()
        data object Correct : OtpVerifyState()
        data object Incorrect : OtpVerifyState()
        data object Error : OtpVerifyState()
    }

    private val _otpVerifyState = MutableStateFlow<OtpVerifyState>(OtpVerifyState.Idle)
    val otpVerifyState: StateFlow<OtpVerifyState> = _otpVerifyState // Expose state to UI

    fun verifyOtp(otp: String) {
        viewModelScope.launch {
            _otpVerifyState.emit(OtpVerifyState.Loading)// Show loading UI
            try {
                val response = retrofitInterfaceLogin.verifyOtp(
                    OtpRequest(
                        email = registrationDetails.email!!,
                        otp = otp
                    )
                )
                if (response.isSuccessful && response.body() != null && response.body()!!.success) {
                    _otpVerifyState.emit(OtpVerifyState.Correct)
                } else if (response.code() == 400) {
                    _otpVerifyState.emit(OtpVerifyState.Incorrect)
                } else {
                    _otpVerifyState.emit(OtpVerifyState.Error)
                }
            } catch (e: Exception) {
                _otpVerifyState.emit(OtpVerifyState.Error)
            }
        }
    }

    // Validate Token
    suspend fun validateToken(token: String): Response<TokenValidationResponse> {
        return try {
            retrofitInterfaceLogin.validateToken(token)
        } catch (e: Exception) {
            Response.error(500,EMPTY_RESPONSE)
        }
    }

    // Registration details
    val registrationDetails = RegistrationDetails()

    data class RegistrationDetails(
        var email: String? = null,
        var password: String? = null,
        var birthday: String? = null,
        var fullName: String? = null,
        var username: String? = null
    )
}

