package com.example.bottomnavigationbar

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {
    private lateinit var v: View
    private lateinit var userName:TextView
    private lateinit var userImage:ImageView
    private lateinit var signOut:Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        v = inflater.inflate(R.layout.fragment_profile,null)
        userName= v.findViewById(R.id.userName)
        userImage = v.findViewById(R.id.userImage)
        signOut = v.findViewById(R.id.signOut)
        val auth = Firebase.auth
        val currentUserName = auth.currentUser!!.displayName
        val image = auth.currentUser!!.photoUrl
        userName.text = currentUserName
        Glide.with(userImage.context).load(image).circleCrop().into(userImage)
        signOut.setOnClickListener {
            val build = AlertDialog.Builder(requireContext())
            build.setTitle("Sign out")
            build.setMessage("Are you sure you want to sign out")
            build.setNegativeButton("Yes"){ dialogInterface: DialogInterface, i: Int ->
                auth.signOut()
                startActivity(Intent(context,SigninActivity::class.java))

            }
            build.setPositiveButton("No"){ dialogInterface: DialogInterface, i: Int ->

            }
            Handler().postDelayed( { build.show() }, 200)


        }

        return v
    }


}