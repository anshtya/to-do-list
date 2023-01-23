package com.example.todolist.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.todolist.databinding.FragmentSignInBinding
import com.example.todolist.ui.home.TodoActivity
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            tvRegister.setOnClickListener {
                val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
                findNavController().navigate(action)
            }

            btLogin.setOnClickListener {
                val signInEmail = binding.etLoginEmail.text.toString()
                val signInPassword = binding.etLoginPassword.text.toString()
                if (signInEmail.isNotEmpty() && signInPassword.isNotEmpty()) {
                    viewModel.signInUser(signInEmail, signInPassword)
                }

                viewLifecycleOwner.lifecycleScope.launch {
                    repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.userAuthorized.collect { currentUser ->
                            when (currentUser) {
                                is Resource.Loading -> {
                                    showProgressBar(true)
                                }
                                is Resource.Success -> {
                                    startActivity(Intent(context, TodoActivity::class.java))
                                    requireActivity().finish()
                                    showProgressBar(false)
                                }
                                is Resource.Error -> {
                                    showProgressBar(false)
                                    Snackbar.make(
                                        view,
                                        "${currentUser.message}",
                                        Snackbar.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    private fun showProgressBar(value: Boolean){
        binding.apply {
            if(value){
                btLogin.visibility = View.INVISIBLE
                emailProgressBar.visibility = View.VISIBLE
            } else {
                btLogin.visibility = View.VISIBLE
                emailProgressBar.visibility = View.INVISIBLE
            }
        }
    }
}