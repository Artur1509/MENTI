package com.example.menti

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.menti.data.Categories
import com.example.menti.data.model.Category
import com.example.menti.ui.ProfileFragment
import com.example.menti.ui.ProfileFragmentDirections
import com.example.menti.util.SearchResultAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import kotlin.coroutines.coroutineContext


// val app hinzugefügt
class FirebaseViewModel(val app: Application) : AndroidViewModel(app) {

    //Firebase Authentication
    val auth = FirebaseAuth.getInstance()
    //Firebase Datenbank
    val firestore = FirebaseFirestore.getInstance()

    //Alle Filterkategorien
    val filterCathegories = Categories()

    //Ausgewählte Filterkategorien
    private val _selectedFilter: MutableLiveData<List<String>> = MutableLiveData()
    val selectedFilter: LiveData<List<String>>
        get() = _selectedFilter

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

    // Favoriten hinzufügen
    fun addFavorites(reference: DocumentReference) {

        Log.e("Firestore", reference.toString())

        var favorit = hashMapOf(
            "reference" to reference
        )
        firestore.collection("Profile").document(_user.value!!.email!!).collection("Favoriten").document().set(favorit).addOnSuccessListener {
            Log.e("Firestore", "Erfolgreich hinzugefügt")
        }

        // Verhindern das Favoriten doppelt gespeichert werden können.
        firestore.collection("Profile").document(_user.value!!.email!!).collection("Favoriten").whereEqualTo("reference", reference)
            .get().addOnSuccessListener { documents ->
                var docSize = documents.size()
                var docIdList: MutableList<String> = mutableListOf()

                if(docSize > 1) {
                    for(document in documents) {
                        docIdList.add(document.id)
                    }
                    firestore.collection("Profile").document(_user.value!!.email!!).collection("Favoriten").document(docIdList.last()).delete().addOnSuccessListener {
                        Log.e("Firestore", "Erfolgreich gelöscht")
                    }
                }
            }
    }

    // Favorit entfernen
    fun removeFavorite(id: String) {
        firestore.collection("Profile").document(_user.value!!.email!!).collection("Favoriten").document(id).delete().addOnSuccessListener {
            Log.e("Firestore", "Erfolgreich gelöscht")
        }
    }

    //Filter anpassen

    fun selectFilterOptions(dataset: List<Category>) {

        var checkedCategories = mutableListOf<String>()
        for(category in dataset) {
            if(category.isChecked) {
                checkedCategories.add(category.name)
            }
        }

        _selectedFilter.value = checkedCategories
        Log.e("Filter", _selectedFilter.value!!.toString())
    }


}