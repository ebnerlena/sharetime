package com.lenaebner.sharetime

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import coil.load
import coil.transform.CircleCropTransformation
import coil.transform.GrayscaleTransformation
import com.lenaebner.sharetime.databinding.PostAdapterFragmentBinding

class PostAdapterFragment : Fragment(R.layout.post_adapter_fragment) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = PostAdapterFragmentBinding.bind(view)

        val adapter = PostAdapter()

        binding.run {


        }
     /*   binding.numberList.adapter = adapter
        binding.numberList.addItemDecoration(
            DividerItemDecoration(
                context,
               DividerItemDecoration.VERTICAL
            )
        )

        binding.addNumber.setOnClickListener {
            val new = adapter.currentList + listOf(adapter.itemCount + 1)
            adapter.submitList(new)
        } */

        binding.img
            .load("https://cdn.pixabay.com/photo/2017/06/26/18/33/mountain-2444712_1280.jpg") {
                placeholder(R.drawable.amy)
                transformations(CircleCropTransformation(), GrayscaleTransformation())
            }
    }

}