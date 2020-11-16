package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.EditProfileFragmentBinding
import com.example.myapplication.databinding.ProfileFragmentBinding

class EditProfileFragment : Fragment(R.layout.edit_profile_fragment){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = EditProfileFragmentBinding.bind(view)
        binding.name.text = "Amy Potter"
        binding.description.text = " This could be your description tap to edit now"

        val websiteIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            setData(Uri.parse("https://www.lenaebner.com"))
        }

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            type = "Text/plain"
            putExtra(Intent.EXTRA_TEXT, "Hey du")
        }

        binding.shareText.setOnClickListener{
            startActivity(sendIntent)
        }

        binding.viewWebsite.setOnClickListener{
            startActivity(websiteIntent)
        }
    }
}