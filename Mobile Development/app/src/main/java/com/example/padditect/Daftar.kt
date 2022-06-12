package com.example.Padditect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_daftar.*

class Daftar : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daftar)

        auth = FirebaseAuth.getInstance()

        btn_daftar.setOnClickListener{
            val nama = etDaftarNama.text.toString().trim()
            val email = etDaftarEmail.text.toString().trim()
            val password = etDaftarPassword.text.toString().trim()
            val password2 = etDaftarPassword2.text.toString().trim()

            if (nama.isEmpty()){
                etDaftarNama.error = "Nama harus diisi"
                etDaftarNama.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                etDaftarEmail.error = "Email harus diisi"
                etDaftarEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length <6){
                etDaftarPassword.error = "Password harus lebih dari 6 karakter"
                etDaftarPassword.requestFocus()
                return@setOnClickListener
            }

            if (password2.isEmpty() || password2.length <6){
                etDaftarPassword2.error = "Password harus sama"
                etDaftarPassword2.requestFocus()
                return@setOnClickListener
            }

            registerUser( email, password)
        }
    }

    private fun registerUser(email: String, password: String,) {
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this){
                if (it.isSuccessful){
                    Intent(this@Daftar, Login::class.java).also{
                        it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(it)
                    }
                } else{
                    Toast.makeText(this, it.exception?.message, Toast.LENGTH_SHORT).show()
                }
            }
    }

/*    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            Intent(this@Daftar, Login::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }
    } */
}