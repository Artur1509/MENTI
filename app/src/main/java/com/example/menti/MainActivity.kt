package com.example.menti

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.menti.databinding.ActivityMainBinding
import com.example.menti.ui.EventsFragment
import com.example.menti.ui.FavoritesFragment
import com.example.menti.ui.HomeFragment
import com.example.menti.ui.MessengerFragment
import com.example.menti.ui.SearchFragment

class MainActivity : AppCompatActivity() {

    //Binding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Home Fragment bei Starten der App
        replaceFragment(HomeFragment())
        binding.bottomNavigation.selectedItemId = R.id.home

        //Bottom Navigation Steuerung
        binding.bottomNavigation.setOnItemSelectedListener {

            when(it.itemId){

                R.id.search -> replaceFragment(SearchFragment())
                R.id.favorites -> replaceFragment(FavoritesFragment())
                R.id.home -> replaceFragment(HomeFragment())
                R.id.messages -> replaceFragment(MessengerFragment())
                R.id.events -> replaceFragment(EventsFragment())

                else -> {

                }
            }
            true
        }
    }


    //Funktion um das navHostFragment durch beliebiges Fragment zu ersetzen.
    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.navHostFragment, fragment)
        fragmentTransaction.commit()


    }
}