package com.lenaebner.sharetime

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lenaebner.sharetime.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*supportFragmentManager.beginTransaction().run {
            add(R.id.fragment_container, ProfileFragment())
            commit()
        } */

        /*val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        } */

           //val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
           // val navController = navHostFragment.navController
    }
}

