package com.lenaebner.sharetime

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.lenaebner.sharetime.databinding.EditProfileFragmentBinding

class EditProfileFragment : Fragment(R.layout.edit_profile_fragment){

    private val arguments: EditProfileFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val binding = EditProfileFragmentBinding.bind(view)

        binding.run {
            name.text = arguments.username

            description.text = " This could be your description tap to edit now"
        }

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