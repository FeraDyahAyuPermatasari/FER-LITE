package com.example.`fera-lite`

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val btnMyProject = findViewById<Button>(R.id.btnMyProject)

        btnMyProject.setOnClickListener {

            val intent = Intent(this, ThirdActivity::class.java)

            startActivity(intent)
        }
    }
}