package com.example.myapplication

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.ProfileFragmentBinding

class ProfileFragment : Fragment(R.layout.profile_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = ProfileFragmentBinding.bind(view)

        binding.description.text = "This could be a useful description text about me. But it isn't. "

        binding.something.setOnClickListener{
            it.setBackgroundColor(getRandomColor())
        }

        binding.editButton.setOnClickListener{
            parentFragmentManager.beginTransaction().run {
                replace(R.id.fragment_container, EditProfileFragment())
                addToBackStack(null)
                commit()
            }
        }
    }

    private val colors = listOf(Color.CYAN, Color.GRAY, Color.MAGENTA, Color.RED, Color.YELLOW)
    private fun getRandomColor(): Int = colors.random()
}