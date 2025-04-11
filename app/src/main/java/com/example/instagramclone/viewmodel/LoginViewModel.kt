package com.example.instagramclone.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instagramclone.model.LoginRequest
import com.example.instagramclone.model.RegistrationRequest
import com.example.instagramclone.network.login.RetrofitInterfaceLogin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val retrofitInterfaceLogin: RetrofitInterfaceLogin) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginState>(LoginState.Idle)
    val uiState: StateFlow<LoginState> = _uiState // Expose state to UI

    fun login(loginRequest: LoginRequest) {
        viewModelScope.launch {
            _uiState.value = LoginState.Loading // Show loading UI
            try {
                val response = retrofitInterfaceLogin.login(loginRequest)
                if (response.isSuccessful && response.body() != null) {
                    _uiState.value = LoginState.Success(token = response.body()!!.token)
                } else {
                    _uiState.value = LoginState.Error("Invalid credentials")
                }
            } catch (e: Exception) {
                _uiState.value = LoginState.Error("Network error")
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
            _uiStateReg.value = LoginState.Loading // Show loading UI
            try {
                val registrationRequest = RegistrationRequest(registrationDetails.email!!,registrationDetails.username!!,registrationDetails.password!!,registrationDetails.fullName!!)
                val response = retrofitInterfaceLogin.register(registrationRequest)
                if (response.isSuccessful && response.body() != null) {
                    _uiStateReg.value = LoginState.Success(token = response.body()!!.token)
                } else {
                    _uiStateReg.value = LoginState.Error("Invalid credentials")
                }
            } catch (e: Exception) {
                _uiStateReg.value = LoginState.Error("Network error")
            }
        }
    }

    // Registration details
    val registrationDetails = RegistrationDetails()
}

data class RegistrationDetails(
    var email: String? = null,
    var password: String? = null,
    var birthday: String? = null,
    var fullName: String? = null,
    var username: String? = null
)