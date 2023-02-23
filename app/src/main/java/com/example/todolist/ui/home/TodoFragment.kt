package com.example.todolist.ui.home

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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.data.network.model.Todo
import com.example.todolist.data.network.model.Response
import com.example.todolist.databinding.FragmentTodoBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TodoFragment : Fragment(), TodoEvents {

    private lateinit var binding: FragmentTodoBinding
    private lateinit var listRecyclerView: RecyclerView
    private lateinit var todoAdapter: TodoAdapter
    private val viewModel: TodoViewModel by activityViewModels()

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

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.todos.collect { response ->
                    when (response) {
                        is Response.Success -> {
                            if (response.data.isEmpty()) {
                                binding.tvEmptyList.visibility = View.VISIBLE
                            } else {
                                todoAdapter.submitList(response.data)
                                binding.tvEmptyList.visibility = View.GONE
                            }
                        }
                        else -> {}
                    }
                }
            }
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val currTodo = todoAdapter.currentList[position]
                viewModel.deleteTodo(currTodo.id)
                Snackbar.make(view, "Article deleted successfully", Snackbar.LENGTH_SHORT).apply {
                    setAction("Undo"){
                        viewModel.insertTodo(currTodo.name)
                    }
                    show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(listRecyclerView)
        }

        binding.btnAddTodo.setOnClickListener {
            val action = TodoFragmentDirections.actionTodoFragmentToTodoAddFragment(
                getString(R.string.add), Todo()
            )
            findNavController().navigate(action)
        }
    }

    private fun showTodoDialog(todo: Todo) {
        val options = arrayOf("Edit","Delete")
        MaterialAlertDialogBuilder(requireContext())
            .setCancelable(true)
            .setItems(options){ _,which ->
                when(options[which]){
                    "Edit" -> onTodoEdit(todo)
                    "Delete" -> viewModel.deleteTodo(todo.id)
                }
            }
            .show()
    }

    private fun setRecyclerView(){
        todoAdapter = TodoAdapter(this)
        listRecyclerView = binding.listRecyclerView
        listRecyclerView.apply{
            adapter = todoAdapter
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
        }
    }

    private fun onTodoEdit(todo: Todo) {
        val action = TodoFragmentDirections.actionTodoFragmentToTodoAddFragment(
            getString(R.string.edit),todo
        )
        findNavController().navigate(action)
    }

    override fun onTodoUpdate(todo: Todo) {
        viewModel.updateTodo(todo)
    }
    override fun callTodoDialog(todo: Todo) {
        showTodoDialog(todo)
    }
}