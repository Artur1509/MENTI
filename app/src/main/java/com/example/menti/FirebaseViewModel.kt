package com.example.menti

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.menti.ui.ProfileFragment
import com.example.menti.ui.ProfileFragmentDirections
import com.example.menti.util.SearchResultAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlin.coroutines.coroutineContext


// val app hinzugefügt
class FirebaseViewModel(val app: Application) : AndroidViewModel(app) {

    val auth = FirebaseAuth.getInstance()
    val firestore = FirebaseFirestore.getInstance()

    //User Profil Dokument Referenz
    lateinit var profileRef : DocumentReference

    private val _user: MutableLiveData<FirebaseUser?> = MutableLiveData()
    val user: LiveData<FirebaseUser?>
        get() = _user


    // Aktueller Nutzer
    init {
        setupUserEnv()
    }
    fun setupUserEnv(){
        _user.value = auth.currentUser

        auth.currentUser?.let {

            // Profil Dokument Referenz aktuallisieren == CurrentUser
            profileRef = firestore.collection("Profile").document(auth.currentUser!!.email!!)
        }
    }

    // -------------------------------------- E-MAIL & PASSWORT ------------------------------------------------- //

    //Einloggen
    fun signIn(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {

            setupUserEnv()
        }
    }

    //Neuen Account erstellen
    fun signUp(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            //Wenn Task fertig ist dann überprüfe z.B. ob der User eingeloggt wurde
            //oder ob es Fehler gab o.Ä.

            setupUserEnv()

            // Nutzerprofil Daten
            val userProfile = hashMapOf(
                "anrede" to "",
                "vorname" to "",
                "name" to "",
                "plz" to "",
                "ort" to "",
                "anschrift" to "",
                "telefonnummer" to "",
                "istEndnutzer" to true
            )

            // Daten in firestore Speichern
            firestore.collection("Profile").document(email).set(userProfile)
        }

    }

    // Ausloggen
    fun signOut(){
        auth.signOut()
        _user.value = auth.currentUser
    }

    // -------------------------------------- FIRESTORE DATABASE ------------------------------------------------- //

    //Nutzerdaten bearbeiten
    fun editProfile(anrede: String, vorname: String, name: String, plz: String, ort: String, anschrift: String, telefonNummer: String, fragment: Fragment) {

        val userProfileUpdate = hashMapOf(
            "anrede" to anrede,
            "vorname" to vorname,
            "name" to name,
            "plz" to plz,
            "ort" to ort,
            "anschrift" to anschrift,
            "telefonnummer" to telefonNummer,
            "istEndnutzer" to true
        )

        // Überschreibe Daten
        firestore.collection("Profile").document(_user.value!!.email!!)
            .set(userProfileUpdate, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(fragment.requireContext(), "Aktualisierung erfolgreich", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(fragment.requireContext(), "Fehler: " + e.message, Toast.LENGTH_SHORT).show()
            }

    }

    // Add Favorites
    fun addFavorites(reference: DocumentReference) {

        var favorit = hashMapOf(
            "reference" to reference
        )
        firestore.collection("Profile").document(_user.value!!.email!!).collection("Favoriten").document().set(favorit)

    }




}