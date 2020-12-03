package com.udacity.shoestore.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.udacity.shoestore.UserPreferences
import com.udacity.shoestore.ui.fragments.view_models.ShoeListViewModel
import kotlinx.coroutines.launch

abstract class BaseFragment<VM: BaseViewModel, B: ViewBinding, R: BaseRepository> : Fragment() {

    protected lateinit var userPreferences: UserPreferences
    protected lateinit var binding : B
    protected lateinit var viewModel : VM
    protected lateinit var mCtx: Context
    //protected val remoteDataSource = RemoteDataSource()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        userPreferences = UserPreferences(mCtx)
        binding = getFragmentBinding(inflater, container)
        val factory = ViewModelFactory(getFragmentRepository())
        viewModel = ViewModelProvider(this, factory).get(getViewModel())

        return binding.root
    }

    /**
     * Only used on 401 error from server, so unused in this application
     */
    fun logout() = lifecycleScope.launch {
        userPreferences.clear()
        requireView().findNavController().navigate(com.udacity.shoestore.R.id.register_destination)
        // TODO: 11/27/20 Revert back to starting fragment
    }

    abstract fun getViewModel() : Class<VM>

    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) : B

    abstract fun getFragmentRepository(): R

}