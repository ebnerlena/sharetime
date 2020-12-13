package com.lenaebner.sharetime

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.lenaebner.sharetime.databinding.EditProfileFragmentBinding

class EditProfileFragment : Fragment(R.layout.edit_profile_fragment){

    private val arguments: EditProfileFragmentArgs by navArgs()
    private val db = Firebase.firestore.collection("users")
    private var user = Person()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = EditProfileFragmentBinding.bind(view)

        db.document(arguments.userId).get().addOnSuccessListener { value ->
            val user = value.toObject<Person>()

            binding.run {

                profilePicture.load(user?.profilePicture) {
                    transformations(CircleCropTransformation())
                }
                description.setText(user?.description)
                name.setText(user?.fullName)

                update.setOnClickListener {
                    val newProperties = mapOf("full_name" to name.text.toString(), "description" to description.text.toString())
                    db.document(arguments.userId).update(newProperties)
                    findNavController().navigate(EditProfileFragmentDirections.editToProfile(Firebase.auth.currentUser?.displayName.toString(), arguments.userId))
                }


            }
        }

      /*  val websiteIntent = Intent().apply {
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
        } */
    }
}