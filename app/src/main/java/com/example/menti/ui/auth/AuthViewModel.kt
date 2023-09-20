package com.example.menti.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.menti.data.AuthRepository
import com.example.menti.data.Resource
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    private val _loginFlow = MutableLiveData<Resource<FirebaseUser>?>(null)
    val loginFlow: LiveData<Resource<FirebaseUser>?> = _loginFlow

    private val _signUpFlow = MutableLiveData<Resource<FirebaseUser>?>(null)
    val signUpFlow: LiveData<Resource<FirebaseUser>?> = _signUpFlow

    val currentUser: FirebaseUser?
        get() = repository.currentUser

    init {
        if(repository.currentUser != null) {
            _loginFlow.postValue(Resource.Success(repository.currentUser!!))
        }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        _loginFlow.postValue(Resource.Loading)
        val result = repository.login(email, password)
        _loginFlow.postValue(result)
    }

    fun signUp(email: String, password: String) = viewModelScope.launch {
        _signUpFlow.postValue(Resource.Loading)
        val result = repository.signUp(email, password)
        _signUpFlow.postValue(result)
    }

    fun logout() {
        repository.logout()
        _loginFlow.postValue(null)
        _signUpFlow.postValue(null)
    }
}