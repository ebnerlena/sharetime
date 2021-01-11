package com.lenaebner.sharetime

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.lenaebner.sharetime.databinding.HomeFragmentBinding
import com.lenaebner.sharetime.databinding.NotificationFragmentBinding

class NotificationFragment : Fragment(R.layout.notification_fragment) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding =  NotificationFragmentBinding.bind(view)
        binding.newsPicture.load(R.drawable.news)
    }
}
