package com.example.menti.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthViewModel: ViewModel() {

    val firebaseAuth = FirebaseAuth.getInstance()

    private val _user: MutableLiveData<FirebaseUser?> = MutableLiveData()
    val user: LiveData<FirebaseUser?>
        get() = _user


    // Aktueller Nutzer
    init {
        setupUserEnv()
    }

    fun setupUserEnv(){
        _user.value = firebaseAuth.currentUser
    }

    // Einloggen
    fun signIn(email: String, password: String) {

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            //Wenn Task fertig ist dann überprüfe z.B. ob der User eingeloggt wurde
            //oder ob es Fehler gab o.Ä.

            setupUserEnv()
        }
    }

    // Neuen Account erstellen
    fun signUp(email: String, password: String) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            //Wenn Task fertig ist dann überprüfe z.B. ob der User eingeloggt wurde
            //oder ob es Fehler gab o.Ä.

            setupUserEnv()
        }

    }

    // Ausloggen
    fun signOut(){
        firebaseAuth.signOut()
        _user.value = firebaseAuth.currentUser
    }
}