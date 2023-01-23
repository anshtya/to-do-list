package com.example.todolist.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.todolist.databinding.FragmentSignUpBinding
import com.example.todolist.ui.home.TodoActivity
import com.example.todolist.util.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private lateinit var binding: FragmentSignUpBinding
    private val viewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            ivBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btSignUp.setOnClickListener {
                val signUpEmail = binding.etSignUpEmail.text.toString()
                val signUpPassword = binding.etSignUpPassword.text.toString()
                if (signUpEmail.isNotEmpty() && signUpPassword.isNotEmpty()) {
                    viewModel.signUpUser(signUpEmail, signUpPassword)
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
                btSignUp.visibility = View.INVISIBLE
                emailProgressBar.visibility = View.VISIBLE
            } else {
                btSignUp.visibility = View.VISIBLE
                emailProgressBar.visibility = View.INVISIBLE
            }
        }
    }
}