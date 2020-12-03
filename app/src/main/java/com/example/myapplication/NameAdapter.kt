package com.example.myapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.myapplication.databinding.NameBinding
import java.lang.IllegalStateException

class NameAdapter: RecyclerView.Adapter<NameAdapter.MultiViewHolder>() {

    private val names = mutableListOf("Hans", "Peter", "Lisl", "Eva", "Chris")

    fun addName() {
        names.add("new Name")
        notifyDataSetChanged()
    }
    override fun getItemViewType(position: Int): Int {
        if(position == 2) {
            return 2
        } else {
            //return R.layout.name
            return 1
        }
    }

    sealed class  MultiViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

        abstract fun bindName(name: String)

        class NameViewHolder2(private val binding: NameBinding): MultiViewHolder(binding){

            override fun bindName(name: String) {
                binding.name.text = name
            }
        }

        class GreetingViewHolder(private val binding: NameBinding): MultiViewHolder(binding){

            override fun bindName(name: String) {
                binding.name.text = "Hello $name"
            }
        }

    }

    class NameViewHolder(private val binding: NameBinding): RecyclerView.ViewHolder(binding.root) {

        fun bindName(name: String) {
            binding.name.text = name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MultiViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = NameBinding.inflate(inflater, parent, false)

        when (viewType) {
            1 -> return MultiViewHolder.NameViewHolder2(binding)
            2 -> return MultiViewHolder.GreetingViewHolder(binding)
            else -> throw  IllegalStateException("Viewtype $viewType is not supported")
        }

        //return MultiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MultiViewHolder, position: Int) {
        val name = names[position]
        holder.bindName(name)
    }

    override fun getItemCount(): Int {
        return names.size
    }
}