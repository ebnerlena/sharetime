package com.example.myapplication

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplication.databinding.HomeFragmentBinding

class HomeFragment : Fragment(R.layout.home_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding =  HomeFragmentBinding.bind(view)

        binding.run {
            actionNav.setOnClickListener {
                findNavController().navigate(R.id.home_to_profile);
            }
            directionNav.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.homeToProfile(9))
            }
            anotherNav.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.homeToProfile(13))
            }
        }
    }
}