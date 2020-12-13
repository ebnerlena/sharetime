package com.lenaebner.sharetime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.onActive
import androidx.compose.ui.platform.setContent
import androidx.core.os.bundleOf
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.lenaebner.sharetime.databinding.ActivityMainBinding

const val REQUEST_CODE = 9999

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        setupActionBarWithNavController(navController)

        val nav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        nav.setupWithNavController(navController)
        nav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val profile = nav.menu.findItem(R.id.profile)

       /* Firebase.firestore.collection("users").document(Firebase.auth.currentUser?.uid.orEmpty()).get().addOnSuccessListener { value ->
            val user = value.toObject<Person>()

            if(user?.profilePicture != "") {
                //geeeeeht ned
                //profile.icon.load(user?.profilePicture).into(profile.icon)
            }
        } */

    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    findNavController(this, R.id.fragment_container).navigate(R.id.homeFragment)
                    true
                }
                R.id.notifications -> {
                    findNavController(this, R.id.fragment_container).navigate(R.id.homeFragment)
                    true
                }
                R.id.profile -> {
                    val user = Firebase.auth.currentUser
                    val bundle = bundleOf("username" to user?.displayName, "userId" to user?.uid)
                    findNavController(this, R.id.fragment_container).navigate(
                        R.id.profileFragment,
                        bundle
                    )
                    true
                }
            }
            false
        }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}

