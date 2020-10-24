package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import com.example.myapplication.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.name.text = "Amy Potter"

        binding.description.text = "Autorin J. K. Rowling hat mit ihren Romanfiguren und deren Zauberwelt etwas " +
                "geschaffen, das so viele Menschen in seinen Bann gezogen hat und auch nach wie vor " +
                "zieht. Immer wieder schlüpfte Daniel Radcliffe in die Rolle des Zauberschülers Harry Potter " +
                "und erlebte mit seinen Freunden Hermine (Emma Watson) und Ron (Rupert Grint) magische Abenteuer."


        binding.something.setOnClickListener{
            it.setBackgroundColor(getRandomColor())
        }

        binding.editButton.setOnClickListener{
            val editIntent = Intent(applicationContext, EditProfileActivity::class.java)
            startActivity(editIntent)
        }

        /*val options = navOptions {
            anim {
                enter = R.anim.slide_in_right
                exit = R.anim.slide_out_left
                popEnter = R.anim.slide_in_left
                popExit = R.anim.slide_out_right
            }
        }

            findNavController().navigate(R.id.editprofile, null, options) */

    }

    private fun getRandomColor(): Int {
        val colors = listOf(Color.CYAN, Color.GRAY, Color.MAGENTA, Color.RED, Color.YELLOW)
        var rnd = Random.nextInt(0,colors.size)
        return colors[rnd]
    }
}

