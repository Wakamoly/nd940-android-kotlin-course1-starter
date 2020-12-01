package com.udacity.shoestore.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.FragmentWelcomeBinding
import com.udacity.shoestore.ui.activities.MainActivity

class WelcomeFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate view and obtain an instance of the binding class.
        val binding: FragmentWelcomeBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_welcome,
                container,
                false
        )

        binding.button.setOnClickListener {
            // TODO: 11/30/20 Make this nav to instructions frag
            requireView().findNavController().navigate(WelcomeFragmentDirections.actionWelcomeDestinationToShoeListFragment())
        }

        return binding.root
    }



}