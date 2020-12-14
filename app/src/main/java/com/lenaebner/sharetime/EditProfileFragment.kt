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

    private val db = Firebase.firestore.collection("users")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = EditProfileFragmentBinding.bind(view)
        val currentUser = Firebase.auth.currentUser

        db.document(currentUser?.uid.toString()).get().addOnSuccessListener { value ->
            val user = value.toObject<Person>() !!

            binding.run {

                profilePicture.load(user?.profilePicture) {
                    transformations(CircleCropTransformation())
                }
                description.setText(user?.description)
                name.setText(user?.fullName)

                update.setOnClickListener {
                    user.fullName = name.text.toString()
                    user.description = description.text.toString()

                    db.document(currentUser?.uid.toString()).set(user)
                    findNavController().navigate(EditProfileFragmentDirections.editToProfile(user.fullName, currentUser?.uid.toString()))
                }
            }
        }
    }
}