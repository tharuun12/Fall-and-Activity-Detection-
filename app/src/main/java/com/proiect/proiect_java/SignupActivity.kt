package com.proiect.proiect_java

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.button.MaterialButton
import com.proiect.proiect_java.R

class SignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val usernameEditText: TextInputEditText = findViewById(R.id.username)
        val passwordEditText: TextInputEditText = findViewById(R.id.password)
        val confirmPasswordEditText: TextInputEditText = findViewById(R.id.confirmPassword)
        val passwordStrengthIndicator: TextView = findViewById(R.id.passwordStrengthIndicator)
        val usernameLayout: TextInputLayout = findViewById(R.id.usernameLayout)
        val passwordLayout: TextInputLayout = findViewById(R.id.passwordLayout)
        val confirmPasswordLayout: TextInputLayout = findViewById(R.id.confirmPasswordLayout)
        val signupButton: MaterialButton = findViewById(R.id.signup_button)

        val sharedPreferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

        // Password strength checking
        passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val passwordStrength = checkPasswordStrength(s.toString())
                passwordStrengthIndicator.text = "Password strength: $passwordStrength"
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        signupButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (username.isEmpty()) {
                usernameLayout.error = "Username cannot be empty"
                return@setOnClickListener
            } else {
                usernameLayout.error = null
            }

            if (password.isEmpty()) {
                passwordLayout.error = "Password cannot be empty"
                return@setOnClickListener
            } else {
                passwordLayout.error = null
            }

            if (confirmPassword != password) {
                confirmPasswordLayout.error = "Passwords do not match"
                return@setOnClickListener
            } else {
                confirmPasswordLayout.error = null
            }

            with(sharedPreferences.edit()) {
                putString("username", username)
                putString("password", password)
                putBoolean("isSignedUp", true)
                apply()
            }

            Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Method to check password strength
    private fun checkPasswordStrength(password: String): String {
        var strength = "Weak"
        if (password.length >= 8) {
            strength = "Medium"
        }
        if (password.length >= 12 && password.any { it.isDigit() } && password.any { it.isUpperCase() }) {
            strength = "Strong"
        }
        return strength
    }
}
