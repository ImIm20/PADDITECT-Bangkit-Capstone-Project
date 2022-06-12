package com.example.Padditect

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_daftar.*
import kotlinx.android.synthetic.main.activity_login.btn_Masuk
import kotlinx.android.synthetic.main.activity_login.*

class Login : AppCompatActivity() {


    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()

        btn_Masuk.setOnClickListener {

            val email = etMasukEmail.text.toString().trim()
            val password = etMasukPassword.text.toString().trim()



            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etMasukEmail.error = "Email harus diisi"
                etMasukEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6) {
                etMasukPassword.error = "Password harus lebih dari 6 karakter"
                etMasukPassword.requestFocus()
                return@setOnClickListener
            }

            loginUser(email, password)
        }

        etDaftarSekarang.setOnClickListener {
            Intent(this@Login, Daftar::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Intent(this@Login, Model::class.java).also { intent ->
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }

    /*fun fDashboard(view: View) {
        val intKeDashboard = Intent(this, Dashboard::class.java)
        startActivity(intKeDashboard)
    }*/

/*    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null) {
            Intent(this@Login, PunyaMenuutama::class.java).also { intent ->
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    } */
}