package com.ahmedesmail111.chaty

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity(), TextWatcher {
    // declaring the variable and initialize it lazily
    private val mAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        editText_email_signIn.addTextChangedListener(this)
        editText_password_signIn.addTextChangedListener(this)

        signIn_button.setOnClickListener{
            val email = editText_email_signIn.text.toString().trim()
            val password = editText_password_signIn.text.toString().trim()

            // if the email is not correct
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                editText_email_signIn.error = "Write a valid Email Address please"
                editText_email_signIn.requestFocus()
                return@setOnClickListener
            }

            // if the password is less than 6 characters
            if (password.length < 6){
                editText_password_signIn.error = "the password should be at least 6 characters"
                editText_password_signIn.requestFocus()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE
            signIn(email,password)

        }


        create_account_button.setOnClickListener{
            val createNewAccountIntent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(createNewAccountIntent)
        }
    }

    private fun signIn(email: String, password: String) {
      // making the Sign In  with Firebase
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener{task ->
            if (task.isSuccessful){
                // intent to go to the main Activity
                val mainActivityIntent = Intent(this@SignInActivity,MainActivity::class.java)
                // pup the activity from the stack
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(mainActivityIntent)

            }else{
                // if there is a problem show that Toast
                Toast.makeText(this,task.exception?.message, Toast.LENGTH_LONG).show()
                progressBar.visibility = View.INVISIBLE
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {
    }
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    // the editTexts will listen or watch this method when their text change
    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
     signIn_button.isEnabled = editText_email_signIn.text.toString().trim().isNotEmpty()
                            && editText_password_signIn.text.toString().trim().isNotEmpty()
    }

    override fun onStart() {
        super.onStart()
        if (mAuth.currentUser?.uid != null){
            // intent to go to the main Activity
            val mainActivityIntent = Intent(this@SignInActivity,MainActivity::class.java)
            startActivity(mainActivityIntent)
        }
    }
}
