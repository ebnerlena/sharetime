package com.lenaebner.sharetime

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.lenaebner.sharetime.databinding.CommentsFragmentBinding

class CommentsFragment : Fragment(R.layout.comments_fragment){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = CommentsFragmentBinding.bind(view)

        //comments adapter
    }
}