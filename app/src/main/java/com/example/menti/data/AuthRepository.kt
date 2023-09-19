package com.example.menti.data

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {

    // Eingeloggter User
    val currentUser: FirebaseUser?

    // User Login
    suspend fun login(email: String, password: String): Resource<FirebaseUser>
    // User Sign Up
    suspend fun signUp(email: String, password: String): Resource<FirebaseUser>

    // Ausloggen
    fun logout()
}