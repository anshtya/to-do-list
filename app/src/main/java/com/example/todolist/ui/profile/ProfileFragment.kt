package com.example.todolist.ui.profile

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
import com.example.todolist.databinding.FragmentProfileBinding
import com.example.todolist.ui.auth.AuthActivity
import com.example.todolist.util.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userProfileDetails.collect { profile ->
                    binding.tvUserEmail.text = profile.data?.userEmail
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userProfile.collect { profile ->
                    when (profile) {
                        is Resource.Loading -> {
                            binding.apply {
                                btnDeleteAcc.visibility = View.INVISIBLE
                                btnLogout.visibility = View.INVISIBLE
                                profileProgressBar.visibility = View.VISIBLE
                            }
                        }
                        is Resource.Success -> {
                            startActivity(Intent(context, AuthActivity::class.java))
                            requireActivity().finish()
                        }
                        is Resource.Error -> {
                            binding.apply {
                                btnDeleteAcc.visibility = View.VISIBLE
                                btnLogout.visibility = View.VISIBLE
                                profileProgressBar.visibility = View.GONE
                            }
                            Snackbar.make(
                                view,
                                "${profile.message}",
                                Snackbar.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }

        binding.apply {
            btnLogout.setOnClickListener {
                viewModel.signOutUser()
            }

            btnDeleteAcc.setOnClickListener {
                viewModel.deleteUser()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.getUser()
    }
}