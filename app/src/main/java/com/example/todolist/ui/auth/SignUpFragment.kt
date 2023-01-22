package com.example.todolist.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.todolist.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint

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

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btSignUp.setOnClickListener {
            val signUpEmail = binding.etSignUpEmail.text.toString()
            val signUpPassword = binding.etSignUpPassword.text.toString()
            if(signUpEmail.isNotEmpty() && signUpPassword.isNotEmpty()){
                Toast.makeText(context, "valid", Toast.LENGTH_SHORT).show()
                viewModel.signUpUser(signUpEmail, signUpPassword)
            }
        }
    }

}