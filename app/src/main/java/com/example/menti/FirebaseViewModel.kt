package com.example.menti

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.menti.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider


// val app hinzugefügt
class FirebaseViewModel(val app: Application) : AndroidViewModel(app) {

    val firebaseAuth = FirebaseAuth.getInstance()

    private val _user: MutableLiveData<FirebaseUser?> = MutableLiveData()
    val user: LiveData<FirebaseUser?>
        get() = _user


    // Aktueller Nutzer
    init {
        setupUserEnv()
    }

    //Google SignIn
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(app.getString(R.string.web_client_id))
        .requestEmail()
        .build()

    private val googleSignInClient = GoogleSignIn.getClient(app, gso)

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {
            if (it.isSuccessful) {
                _user.value = firebaseAuth.currentUser
            } else {
                _user.value = null
            }
        }
    }

    ///////

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