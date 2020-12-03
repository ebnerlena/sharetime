package com.example.myapplication

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.myapplication.databinding.ProfileFragmentBinding

class ProfileFragment : Fragment(R.layout.profile_fragment) {

    private val args: ProfileFragmentArgs by navArgs()
    private val adapter = PostAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = ProfileFragmentBinding.bind(view)


        binding.run {
            name.text = adapter.fakePosts[args.userId-1].userName + " " +args.userId
            description.text = "This could be a useful description text about me. But it isn't. "

            profilePicture.load(adapter.fakePosts[args.userId-1].profileImage)
            something.setOnClickListener{
                it.setBackgroundColor(getRandomColor())
            }
        }

        binding.editButton.setOnClickListener{

            //Navigate with directions
            findNavController().navigate(ProfileFragmentDirections.profileToEditProfile(adapter.fakePosts[args.userId-1].userName))
        }
    }

    private val colors = listOf(Color.CYAN, Color.GRAY, Color.MAGENTA, Color.RED, Color.YELLOW)
    private fun getRandomColor(): Int = colors.random()
}