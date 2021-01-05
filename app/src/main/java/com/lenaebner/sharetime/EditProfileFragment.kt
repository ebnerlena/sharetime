package com.lenaebner.sharetime

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.CircleCropTransformation
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.lenaebner.sharetime.databinding.EditProfileFragmentBinding
import com.lenaebner.sharetime.databinding.NewPostFragmentBinding

class EditProfileFragment : Fragment(R.layout.edit_profile_fragment){

    private val db = Firebase.firestore
    private var chosenImgUri: Uri? = null
    private lateinit var binding: EditProfileFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = EditProfileFragmentBinding.bind(view)
        val currentUser = Firebase.auth.currentUser

        db.collection("people").document(currentUser?.uid.toString()).addSnapshotListener{ value, _ ->
            val user = value?.toObject<Person>()
            binding.run {

                if(!user?.profilePicture.isNullOrEmpty()) {
                    profilePicture.load(user?.profilePicture)
                } else {
                    uploadPicture.visibility = VISIBLE
                }

                profilePicture.setOnClickListener {
                    launchGallery()
                }

                description.setText(user?.description)
                name.setText(user?.fullName)
                userName.setText(user?.username)
                location.setText(user?.location)

                update.setOnClickListener {
                    uploadImg()

                    if(user!= null) {
                        user.fullName = name.text.toString()
                        user.username = userName.text.toString()
                        user.description = description.text.toString()
                        user.location = location.text.toString()


                        val updateUser = db.collection("people").document(currentUser?.uid.toString()).set(user)
                        updateUser.addOnSuccessListener {
                            profilePicture.load(user.profilePicture)
                            findNavController().navigate(EditProfileFragmentDirections.editToProfile(user.fullName, currentUser?.uid.toString()))
                        }

                        updateUser.addOnFailureListener {
                            Snackbar.make(binding.root,"Problems updating your profile... check input and try again",  Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun launchGallery() {
        val pickIntent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        pickIntent.type = "image/*"
        startActivityForResult(pickIntent, MainActivity.PICK_IMAGE_REQUEST)
    }

    private fun uploadImg() {
        val imgUri = chosenImgUri ?: return
        val img = requireContext()
                .contentResolver
                .openInputStream(imgUri) ?: return

        val currentUser = Firebase.auth.currentUser

        db.collection("people").document(currentUser?.uid.toString()).get().addOnSuccessListener {
            val person = it?.toObject<Person>()

            if (person != null) {

                val imageRef = Firebase.storage.reference.child("profile-images/${currentUser?.uid.toString()}")
                val uploadTask = imageRef.putStream(img)

                uploadTask.addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { url ->
                        person.profilePicture = url.toString()
                        db.collection("people").document(currentUser?.uid.toString()).set(person)
                    }
                }
                uploadTask.addOnFailureListener {
                 Snackbar.make(binding.root ,"Problems updating your profile image... try again", Snackbar.LENGTH_SHORT).show()
                }

            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, result: Intent?) {
        super.onActivityResult(requestCode, resultCode, result)
        if (requestCode == MainActivity.PICK_IMAGE_REQUEST) {
            chosenImgUri = result?.data

            if (chosenImgUri != null) {
                binding.profilePicture.load(chosenImgUri)
            }
        }
    }
}