package com.example.menti.data

import com.example.menti.data.util.await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    // User Login
    override suspend fun login(email: String, password: String): Resource<FirebaseUser> {
        return try {

            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)

        } catch(e: Exception) {

            e.printStackTrace()
            Resource.Failure(e)

        }
    }

    // User Signup
    override suspend fun signUp(email: String, password: String): Resource<FirebaseUser> {
        return try {

            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            Resource.Success(result.user!!)

        } catch(e: Exception) {

            e.printStackTrace()
            Resource.Failure(e)

        }
    }

    // User Logout
    override fun logout() {
        firebaseAuth.signOut()
    }
}