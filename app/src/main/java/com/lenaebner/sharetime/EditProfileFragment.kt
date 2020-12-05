package com.lenaebner.sharetime

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.lenaebner.sharetime.databinding.EditProfileFragmentBinding

class EditProfileFragment : Fragment(R.layout.edit_profile_fragment){

    private val arguments: EditProfileFragmentArgs by navArgs()
    private var user = Person()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val binding = EditProfileFragmentBinding.bind(view)

        val users = Firebase.firestore.collection("users")
        val me = users.whereEqualTo("full_name", arguments.username).addSnapshotListener { value, error ->
            val users = value?.toObjects<Person>().orEmpty()
            user = users?.get(0)

            binding.run {
                name.text = user.fullName
                profilePicture.load(user.profilePicture)

                description.text = " This could be your description tap to edit now"
            }
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