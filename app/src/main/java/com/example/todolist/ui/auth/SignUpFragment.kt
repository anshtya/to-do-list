package com.example.todolist.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.data.network.model.Response
import com.example.todolist.databinding.FragmentSignUpBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userAuthorizedEmail.collect { response ->
                    when (response) {
                        is Response.Loading -> {
                            binding.apply {
                                btSignUp.visibility = View.INVISIBLE
                                emailProgressBar.visibility = View.VISIBLE
                            }
                        }
                        is Response.Success -> {
                            findNavController().navigate(R.id.action_todoFragment)
                        }
                        is Response.Error -> {
                            binding.apply {
                                btSignUp.visibility = View.VISIBLE
                                emailProgressBar.visibility = View.GONE
                            }
                            Snackbar.make(
                                view, "${response.e?.message}", Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        binding.apply {

            ivBack.setOnClickListener {
                findNavController().navigateUp()
            }

            btSignUp.setOnClickListener {
                val signUpEmail = binding.etSignUpEmail.text.toString()
                val signUpPassword = binding.etSignUpPassword.text.toString()
                if (signUpEmail.isNotEmpty() && signUpPassword.isNotEmpty()) {
                    viewModel.signUpUser(signUpEmail, signUpPassword)
                } else {
                    Snackbar.make(view, "Fill the required fields", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        binding.apply {
            etSignUpEmail.text = null
            etSignUpPassword.text = null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}