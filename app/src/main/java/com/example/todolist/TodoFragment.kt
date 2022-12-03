package com.example.todolist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.adapter.TodoAdapter
import com.example.todolist.adapter.TodoOps
import com.example.todolist.databinding.FragmentTodoBinding
import com.example.todolist.viewmodel.TodoViewModel
import com.example.todolist.viewmodel.TodoViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodoFragment : Fragment(), TodoOps {

    private lateinit var binding: FragmentTodoBinding
    private lateinit var listRecyclerView: RecyclerView
    private val adapter = TodoAdapter(this)
    private val viewmodel: TodoViewModel by activityViewModels{
        TodoViewModelFactory(
            (activity?.application as TodoApplication).database
                .todoDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        lifecycle.coroutineScope.launch(Dispatchers.IO) {
            viewmodel.allTodo.collect() {
                adapter.submitList(it)
            }
        }

        binding.btnAddTodo.setOnClickListener {
            val action = TodoFragmentDirections.actionTodoFragmentToTodoAddFragment()
            findNavController().navigate(action)
        }
    }

    private fun setRecyclerView(){
        listRecyclerView = binding.listRecyclerView
        listRecyclerView.layoutManager = LinearLayoutManager(context)
        listRecyclerView.adapter = adapter
        listRecyclerView.setHasFixedSize(true)
    }

    override fun onTodoUpdate(todoId: Int, todoName: String, todoIsDone: Boolean) {
        viewmodel.updateTodo(todoId, todoName, todoIsDone)
    }
}