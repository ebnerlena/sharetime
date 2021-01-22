 package com.lenaebner.sharetime

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.onActive
import androidx.compose.ui.platform.setContent
import androidx.core.graphics.drawable.toBitmap
import androidx.core.os.bundleOf
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import coil.imageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.lenaebner.sharetime.databinding.ActivityMainBinding
import com.lenaebner.sharetime.databinding.NewPostFragmentBinding

class MainActivity : AppCompatActivity() {

    private val db = Firebase.firestore.collection("people")
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding =  ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val appBarConfig = AppBarConfiguration(topLevelDestinationIds = setOf(
                R.id.homeFragment,
                R.id.loginFragment
        ))

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Home"

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        binding.toolbar.setupWithNavController(navController, appBarConfig)
        binding.bottomNav.setupWithNavController(navController)
        binding.bottomNav.itemIconTintList = null
        binding.bottomNav.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        //loading profile image in navbar if present, else fallback image
        Firebase.auth.addAuthStateListener { auth ->

          db.document(Firebase.auth.currentUser?.uid.toString()).addSnapshotListener { value, _ ->
              val imageLoader = imageLoader
              val currentUser = value?.toObject<Person>()
                val request = ImageRequest.Builder(this)
                        .data(currentUser?.profilePicture)
                        .fallback(R.drawable.person)
                        .transformations(CircleCropTransformation())
                        .target { drawable ->
                            val profile = binding.bottomNav.menu.findItem(R.id.profileFragment)
                            profile.icon = drawable
                        }
                        .build()

                imageLoader.enqueue(request)
            }
        }
    }

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val navController = findNavController(this, R.id.fragment_container)
            when (item.itemId) {
                R.id.profileFragment -> {
                    val authUser = Firebase.auth.currentUser
                    val userRef = db.document(authUser?.uid.toString()).get()
                    userRef.addOnSuccessListener {
                        val user = it.toObject<Person>()
                        val bundle = bundleOf("username" to user?.fullName, "userId" to authUser?.uid.toString())
                        navController.navigate(
                                R.id.profileFragment,
                                bundle
                        )
                        true
                    }
                    userRef.addOnFailureListener {
                        Snackbar.make(binding.root, "Problems with your user account...", Snackbar.LENGTH_SHORT).show()
                        false
                    }

                }
                else -> item.onNavDestinationSelected(navController)
            }
            false
        }

    companion object {
        const val REQUEST_CODE = 9999
        const val PICK_IMAGE_REQUEST = 8888
    }

}

