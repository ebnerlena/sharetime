package com.lenaebner.sharetime

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.lenaebner.sharetime.databinding.RecyclerViewFragmentBinding

class RecyclerViewFragment : Fragment(R.layout.recycler_view_fragment)
{

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = RecyclerViewFragmentBinding.bind(view)

        val adapter = NameAdapter()

        binding.fragmentList.adapter = adapter;
        binding.fragmentList.layoutManager = LinearLayoutManager(context)

        binding.addName.setOnClickListener {
            adapter.addName()
        }

    }
}