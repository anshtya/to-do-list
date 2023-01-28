package com.example.todolist.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.todolist.databinding.FragmentSignInBinding
import com.example.todolist.ui.home.TodoActivity
import com.example.todolist.util.Resource
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : Fragment() {

    private lateinit var binding: FragmentSignInBinding
    private val viewModel: AuthViewModel by activityViewModels()

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userAuthorized.collect { currentUser ->
                    when (currentUser) {
                        is Resource.Loading -> {
                            binding.apply {
                                btLogin.visibility = View.INVISIBLE
                                emailProgressBar.visibility = View.VISIBLE
                            }
                        }
                        is Resource.Success -> {
                            startActivity(Intent(context, TodoActivity::class.java))
                            requireActivity().finish()
                        }
                        is Resource.Error -> {
                            binding.apply {
                                btLogin.visibility = View.VISIBLE
                                emailProgressBar.visibility = View.GONE
                            }
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

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userAuthorizedGoogle.collect { currentUser ->
                    when (currentUser) {
                        is Resource.Loading -> {
                            binding.apply {
                                googleProgressBar.visibility = View.VISIBLE
                                btLoginGoogle.visibility = View.INVISIBLE
                            }
                        }
                        is Resource.Success -> {
                            startActivity(Intent(context, TodoActivity::class.java))
                            requireActivity().finish()
                        }
                        is Resource.Error -> {
                            binding.apply {
                                btLoginGoogle.visibility = View.VISIBLE
                                googleProgressBar.visibility = View.GONE
                            }
                            if(currentUser.message != null){
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

        binding.apply {

            tvRegister.setOnClickListener {
                val action = SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
                findNavController().navigate(action)
            }

            btLoginGoogle.setOnClickListener {
                val signInIntent = googleSignInClient.signInIntent
                resultLauncher.launch(signInIntent)
            }

            btLogin.setOnClickListener {
                val signInEmail = binding.etLoginEmail.text.toString()
                val signInPassword = binding.etLoginPassword.text.toString()
                if (signInEmail.isNotEmpty() && signInPassword.isNotEmpty()) {
                    viewModel.signInUser(signInEmail, signInPassword)
                } else {
                    Snackbar.make(view, "Fill the required fields", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        binding.apply {
            etLoginEmail.text = null
            etLoginPassword.text = null
        }
    }

    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                viewModel.signInWithGoogle(account)
            } catch (e: ApiException) {
                viewModel.signInWithGoogleCancelled()
                Snackbar.make(
                    requireParentFragment().requireView(),
                    "Request Failed",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
}