package com.ahmedesmail111.chaty

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.core.graphics.drawable.toDrawable
import com.ahmedesmail111.chaty.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() , TextWatcher {
   // declaring the variable and initialize it lazily
     private val mAuth:FirebaseAuth by lazy {
         FirebaseAuth.getInstance()
     }

    // initializing an instance of the firebase fireStore
    private val fireStoreInstance : FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
    // create a collection of name "users" and a document named by the UID of the current user
    val currentUserDocRef: DocumentReference
    get() = fireStoreInstance.document("users/${mAuth.currentUser?.uid.toString()}")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // to make the editTexts Listen or watch the  methods of the watcher
        editText_name_signUp.addTextChangedListener(this@SignUpActivity)
        editText_email_signUp.addTextChangedListener(this@SignUpActivity)
        editText_password_signUp.addTextChangedListener(this@SignUpActivity)


        // set a listener on the button to be able to make a new user with Firebase
        signUp_button.setOnClickListener{
            val name    =   editText_name_signUp.text.toString().trim()
            val email   = editText_email_signUp.text.toString().trim()
            val password= editText_password_signUp.text.toString().trim()

            // if the email is not correct
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                editText_email_signUp.error = "Write a valid Email Address please"
                editText_email_signUp.requestFocus()
                return@setOnClickListener
            }

            // if the password is less than 6 characters
            if (password.length < 6){
                editText_password_signUp.error = "the password should be at least 6 characters"
                editText_password_signUp.requestFocus()
                return@setOnClickListener
            }
            //show the progress bar
            progressBar.visibility = View.VISIBLE
            // calling this function to make a new account
          createNewAccount(name,email,password)

        }




    }
    // a function to create a new Account using Firebase
    private fun createNewAccount(name: String, email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task ->
            if (task.isSuccessful){
                val newUser = User(name)
                currentUserDocRef.set(newUser)
                // intent to go to the main Activity
                val mainActivityIntent = Intent(this@SignUpActivity,MainActivity::class.java)
                // pup the activity from the stack
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(mainActivityIntent)
            }else{
                Toast.makeText(this,task.exception?.message, Toast.LENGTH_LONG).show()
                progressBar.visibility = View.INVISIBLE
            }
        }
    }


    override fun afterTextChanged(s: Editable?) {
    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }
    // the editTexts will watch or listen to this function
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        // enable the button if all editTexts are not empty (spaces are not count)
         signUp_button.isEnabled = editText_email_signUp.text.trim().isNotEmpty()
                 && editText_name_signUp.text.trim().isNotEmpty()
                 && editText_password_signUp.text.trim().isNotEmpty()
    }
}
