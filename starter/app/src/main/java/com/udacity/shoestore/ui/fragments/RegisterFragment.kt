package com.udacity.shoestore.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.udacity.shoestore.*
import com.udacity.shoestore.base.BaseFragment
import com.udacity.shoestore.databinding.FragmentRegisterBinding
import com.udacity.shoestore.db.ShoeStoreDatabase
import com.udacity.shoestore.db.dao.ShoeDao
import com.udacity.shoestore.db.dao.UserDao
import com.udacity.shoestore.repositories.RegisterRepository
import com.udacity.shoestore.ui.fragments.view_models.RegisterViewModel

class RegisterFragment : BaseFragment<RegisterViewModel, FragmentRegisterBinding, RegisterRepository>() {

    private lateinit var userDao: UserDao
    private lateinit var shoeDao: ShoeDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCtx = requireContext()
        userDao = ShoeStoreDatabase.invoke(mCtx).getUserDao()
        shoeDao = ShoeStoreDatabase.invoke(mCtx).getShoeDao()
        requireActivity().actionBar?.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        setOnClickListeners()
    }

    private fun register(){
        if (binding.etUsername.text.isEmpty()){
            binding.etUsername.setText(getString(R.string.example_username))
        }
        if (binding.etEmail.text.isEmpty()){
            binding.etEmail.setText(getString(R.string.example_email_text))
        }
        if (binding.passwordEt.text.isEmpty()){
            binding.passwordEt.setText(getString(R.string.example_password))
        }

        val password = binding.passwordEt.text.toString()
        val email = binding.etEmail.text.toString()
        val username = binding.etUsername.text.toString()

        viewModel.login(email, password, username)
    }

    private fun setOnClickListeners(){
        binding.registerButton.setOnClickListener {
            register()
        }
        binding.loginButton.setOnClickListener {
            register()
        }
    }

    private fun subscribeObservers(){

        viewModel.loginResponse.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Loading -> loading()
                is Resource.Failure -> handleApiError(it)
                is Resource.Success -> loginSuccess()
            }
        })

        viewModel.regResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> loading()
                is Resource.Failure -> handleApiError(it)
                is Resource.Success -> loginSuccess()
            }
        })

    }

    private fun loading(){
        requireView().snackbar(getString(R.string.logging_you_in_text))
        binding.loadingProgress.visible(true)
    }

    private fun loginSuccess(){
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            requireView().snackbar(getString(R.string.login_complete_text))
            requireView().findNavController().navigate(RegisterFragmentDirections.actionRegisterDestinationToWelcomeFragment())
        }
        handler.postDelayed(runnable, 2000)
    }

    override fun getViewModel() = RegisterViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentRegisterBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = RegisterRepository(userPreferences, userDao, shoeDao)


}