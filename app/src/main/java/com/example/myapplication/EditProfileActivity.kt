package com.example.myapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.myapplication.databinding.ActivityEditProfileBinding


class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //data binding
        //val binding2 : ActivityEditProfileBinding = DataBindingUtil.setContentView(this,R.layout.activity_edit_profile)

    }
        //setContentView(binding.root)

        /* binding.setName("Amy Potter")
        binding.setDescription((" This could be your description tap to edit now"))


       /* binding.textView.setOnClickListener{
            findNavController(R.id.profile_Fragment).navigate((R.id.mainActivity))
        } */

         */
       /* val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.add(R.id.profile_Fragment, SampleFragment())
        fragmentTransaction.commit() */

        /*binding.textView.setOnClickListener{
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        }*/

}