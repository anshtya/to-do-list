package com.example.todolist.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.todolist.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity(R.layout.activity_auth) {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.auth_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

    }
}