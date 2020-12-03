package com.lenaebner.sharetime

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.lenaebner.sharetime.databinding.HomeFragmentBinding

class HomeFragment : Fragment(R.layout.home_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding =  HomeFragmentBinding.bind(view)
        val adapter = PostAdapter()

        binding.postList.adapter = adapter

        val list = adapter.currentList + listOf(1,2,3,4,5)
        adapter.submitList(list)
    }
}