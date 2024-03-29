package com.example.bottomnavigationbar

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.example.bottomnavigationbar.daos.UserDao
import com.example.bottomnavigationbar.models.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SigninActivity : AppCompatActivity() {

    private lateinit var progressBar:ProgressBar
    private val RC_SIGN_IN: Int = 1
    private lateinit var googleSignINClient:GoogleSignInClient
    private lateinit var auth :FirebaseAuth
    private lateinit var  signInButton:SignInButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignINClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth
        signInButton = findViewById(R.id.signInButton)
        progressBar = findViewById(R.id.progressBar)


        signInButton.setOnClickListener{
            signIn()
        }


    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun signIn(){
        val signInIntent = this.googleSignINClient.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private  fun handleSignInResult(completedTask: Task<GoogleSignInAccount>?) {

        try {
            val account =
                completedTask?.getResult(ApiException::class.java)!!
            Log.d(TAG,"firebaseAuthWithGoogle"+account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        }catch (e:ApiException){
            Log.d(TAG,"signInResult:failed code"+e.statusCode)
        }

    }


    private  fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        signInButton.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

        GlobalScope.launch(Dispatchers.IO) {
            val auth = auth.signInWithCredential(credential).await()
            val firebaseUser = auth.user
            withContext(Dispatchers.Main){
                updateUI(firebaseUser)
            }
        }

    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        if(firebaseUser!=null){

            val user =
                firebaseUser.photoUrl?.let {
                    User(firebaseUser.uid,firebaseUser.displayName,
                        it.toString())
                }
            val usersDao = UserDao()
            usersDao.addUser(user)

            val mainActivityIntent = Intent(this,MainActivity::class.java)
            startActivity(mainActivityIntent)
            finish()
        }else{
            signInButton.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }


}