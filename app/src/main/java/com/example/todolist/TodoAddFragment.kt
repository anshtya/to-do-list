package com.example.todolist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.todolist.databinding.FragmentTodoAddBinding

class TodoAddFragment : Fragment() {

    private lateinit var binding: FragmentTodoAddBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTodoAddBinding.inflate(inflater, container, false)
        return binding.root
    }
}