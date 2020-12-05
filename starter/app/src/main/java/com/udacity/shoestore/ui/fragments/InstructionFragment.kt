package com.udacity.shoestore.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentInstructionBinding
import com.udacity.shoestore.databinding.FragmentWelcomeBinding

class InstructionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.hide()
        // Inflate view and obtain an instance of the binding class.
        val binding = FragmentInstructionBinding.inflate(inflater,container,false)

        binding.addShoeButton.setOnClickListener {
            requireView().findNavController().navigate(InstructionFragmentDirections.actionInstructionFragmentToShoelistDestination())
        }

        return binding.root
    }

}