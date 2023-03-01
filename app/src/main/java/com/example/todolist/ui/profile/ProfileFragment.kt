package com.example.todolist.ui.profile

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
import com.example.todolist.domain.model.Response
import com.example.todolist.databinding.FragmentProfileBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userProfileDetails.collect { profile ->
                    binding.tvUserEmail.text = profile?.email
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userProfile.collect { response ->
                    when (response) {
                        is Response.Loading -> {
                            binding.apply {
                                btnDeleteAcc.visibility = View.INVISIBLE
                                btnLogout.visibility = View.INVISIBLE
                                profileProgressBar.visibility = View.VISIBLE
                            }
                        }
                        is Response.Success -> {
                            val action =
                                ProfileFragmentDirections.actionProfileFragmentToSignInFragment()
                            findNavController().navigate(action)
                        }
                        is Response.Error -> {
                            binding.apply {
                                btnDeleteAcc.visibility = View.VISIBLE
                                btnLogout.visibility = View.VISIBLE
                                profileProgressBar.visibility = View.GONE
                            }
                            Snackbar.make(
                                view,
                                "${response.e?.message}",
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

    private fun setToolbar() {
        binding.profileFragmentToolbar.title = findNavController().currentDestination?.label
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}