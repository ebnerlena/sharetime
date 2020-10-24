package com.example.myapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.myapplication.databinding.ActivityEditProfileBinding


class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivityEditProfileBinding = DataBindingUtil.setContentView(this,R.layout.activity_edit_profile)
        //setContentView(binding.root)

        binding.setName("Amy Potter")
        binding.setDescription((" This could be your description tap to edit now"))

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