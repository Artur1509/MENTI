package com.example.menti

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
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


        binding.bottomNavigation.selectedItemId = R.id.home

        //Bottom Navigation Steuerung
        binding.bottomNavigation.setOnItemSelectedListener {

            when (it.itemId) {

                R.id.search -> findNavController(R.id.navHostFragment).navigate(R.id.searchFragment)
                R.id.favorites -> findNavController(R.id.navHostFragment).navigate(R.id.favoritesFragment)
                R.id.home -> findNavController(R.id.navHostFragment).navigate(R.id.homeFragment)
                R.id.messages -> findNavController(R.id.navHostFragment).navigate(R.id.messengerFragment)
                R.id.events -> findNavController(R.id.navHostFragment).navigate(R.id.eventsFragment)

                else -> {

                }
            }
            true
        }

    }

}