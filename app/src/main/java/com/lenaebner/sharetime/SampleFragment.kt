package com.lenaebner.sharetime

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.lenaebner.sharetime.databinding.EditProfileFragmentBinding
import com.lenaebner.sharetime.databinding.ProfileFragmentBinding

class SampleFragment : Fragment (R.layout.profile_fragment) {

    //must not implement cause view is passed in constructor
    /*override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

        return inflater.inflate(R.layout.activity_edit_profile, container, false)
    } */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = EditProfileFragmentBinding.bind(view)

        //binding.setName("Amy Potter")
        //binding.setDescription((" This could be your description tap to edit now"))

        val websiteIntent = Intent().apply {
            action = Intent.ACTION_VIEW
            setData(Uri.parse("https://www.lenaebner.com"))
        }

        //sharing with whatsapp fails
        //sharing with gmail
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