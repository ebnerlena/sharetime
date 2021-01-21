package com.lenaebner.sharetime

import android.content.Intent
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.compose.ui.graphics.imageFromResource
import androidx.core.view.GravityCompat.apply
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.lenaebner.sharetime.databinding.NewPostFragmentBinding
import com.lenaebner.sharetime.firestore.allPosts
import com.lenaebner.sharetime.firestore.currentUser
import com.lenaebner.sharetime.firestore.users

class NewPostFragment : Fragment(R.layout.new_post_fragment) {
    private val db = Firebase.firestore
    private lateinit var binding: NewPostFragmentBinding
    private val storage = Firebase.storage
    private var chosenImgUri: Uri? = null
    private var uploadTask : UploadTask? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = NewPostFragmentBinding.bind(view)

        val currentUser = db.currentUser()

        if(currentUser == null) {
            findNavController().navigate(NewPostFragmentDirections.newPostToLogin())
        }

        binding.run {
            postPicture.setOnClickListener {
                launchGallery()
            }
            progressBar.isVisible = false
            post.setOnClickListener {

                if(description.text.toString().isNotBlank()) {
                    uploadImg()
                }
            }
        }
    }

    private fun showLoading(loading: Boolean = true) {
        binding.run {
            progressBar.isVisible = loading
            notLoading.isVisible = !loading
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
        val imgUri = chosenImgUri?: return
        val img = requireContext()
                .contentResolver
                .openInputStream(imgUri) ?: return

        val description = binding.description.text
        val currentUser = db.currentUser()

        db.users().document(currentUser.id).get().addOnSuccessListener {
            val person = it?.toObject<Person>()
            val author = it?.toObject<Author>()
            author?.uid = currentUser.id

            if (person != null && author != null) {
                val newPostRef = db.allPosts().document()
                val imageRef = storage.reference.child("post-images/${newPostRef.id}")
                uploadTask = imageRef.putStream(img)
                showLoading()

                uploadTask?.addOnSuccessListener {
                    imageRef.downloadUrl.addOnSuccessListener { url ->
                        val newPost = Post(
                            imageUrl = url.toString(),
                            text = description.toString(),
                            author = author
                        )

                        Snackbar.make(binding.root ,"Successfully uploaded post", Snackbar.LENGTH_SHORT ).show()

                        newPostRef.set(newPost).addOnSuccessListener {
                            person.posts = person.posts + newPostRef

                            db.users().document(currentUser.id).set(person).addOnSuccessListener {
                                findNavController().navigate(
                                    NewPostFragmentDirections.newPostSubmit(currentUser.id)
                                )
                            }
                        }
                    }
                }

                //doesn't solve problem of clicking on back arrow on device - crash
                uploadTask?.addOnFailureListener {
                   Snackbar.make(binding.root,"Problems uploading post image... try again", Snackbar.LENGTH_SHORT ).show()
                }
                uploadTask?.addOnCanceledListener {
                    it.reference.delete()
                    newPostRef.delete()
                }
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, result: Intent?) {
        super.onActivityResult(requestCode, resultCode, result)
        if (requestCode == MainActivity.PICK_IMAGE_REQUEST) {
            chosenImgUri = result?.data

            if (chosenImgUri != null) {
                binding.postPicture.load(chosenImgUri)
                binding.chooseImageLabel.isVisible = false
                binding.galleryIcon.isVisible = false
            }
        }
    }

    override fun onDestroy() {
        uploadTask?.cancel()
        super.onDestroy()
    }

}