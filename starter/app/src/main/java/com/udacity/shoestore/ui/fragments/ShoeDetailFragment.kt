package com.udacity.shoestore.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.udacity.shoestore.R
import com.udacity.shoestore.Resource
import com.udacity.shoestore.base.BaseFragment
import com.udacity.shoestore.base.ViewModelFactory
import com.udacity.shoestore.databinding.FragmentShoeDetailBinding
import com.udacity.shoestore.db.ShoeStoreDatabase
import com.udacity.shoestore.db.dao.ShoeDao
import com.udacity.shoestore.db.entities.ShoeEntity
import com.udacity.shoestore.repositories.ShoeListRepository
import com.udacity.shoestore.snackbar
import com.udacity.shoestore.ui.fragments.view_models.ShoeListViewModel
import com.udacity.shoestore.visible
import timber.log.Timber

class ShoeDetailFragment : BaseFragment<ShoeListViewModel, FragmentShoeDetailBinding, ShoeListRepository>() {

    private lateinit var shoeDao: ShoeDao
    private val shoeViewModel: ShoeListViewModel by activityViewModels { ViewModelFactory(getFragmentRepository()) }
    private val args: ShoeDetailFragmentArgs by navArgs()
    private lateinit var shoeEntity: ShoeEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.shoe_details_text)
        mCtx = requireContext()
        shoeDao = ShoeStoreDatabase(mCtx).getShoeDao()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        subscribeObservers()
    }

    private fun initView(){
        binding.fragment = this
        binding.viewmodel = shoeViewModel
        if (args.shoeEntity != null){
            /**
             * Shoe listing clicked, editing existing
             */
            shoeEntity = args.shoeEntity!!
            binding.entity = shoeEntity
            Timber.d(shoeEntity.id.toString())
        }else{
            /**
             * Floating button to add new shoe pressed, not editing a existing listing
             */
            Timber.d("ShoeEntity null!")
            (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.add_shoe_text)
            binding.entity = ShoeEntity("",0.0,"","", emptyList())
        }
    }

    fun saveShoe(newShoeEntity: ShoeEntity){
        val imageList: MutableList<String> = ArrayList()
        for (imageURL in newShoeEntity.images){
            if (imageURL.isNotEmpty()){
                imageList.add(imageURL)
            }
        }
        newShoeEntity.images = imageList
        if (::shoeEntity.isInitialized){
            shoeViewModel.updateShoe(newShoeEntity, shoeEntity.id)
        }else{
            shoeViewModel.saveShoe(newShoeEntity)
        }
    }

    private fun subscribeObservers(){
        shoeViewModel.saveFinishedEvent.observe(viewLifecycleOwner, Observer {
            when(it){
                true -> {
                    findNavController().navigate(ShoeDetailFragmentDirections.actionShoeDetailDestinationToShoelistDestination())
                }
            }
        })

        shoeViewModel.result.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> {
                    binding.saveShoeButton.isEnabled = false
                    binding.shoeDetailProgress.visible(true)
                }
                is Resource.Failure -> {
                    binding.saveShoeButton.isEnabled = true
                    binding.shoeDetailProgress.visible(false)
                    requireView().snackbar("Shoe save failed!")
                }
                is Resource.Success -> {
                    binding.saveShoeButton.isEnabled = true
                    binding.shoeDetailProgress.visible(false)
                    requireView().snackbar("Success!")
                    shoeViewModel.saveFinished()
                }
            }
        })
    }

    override fun getViewModel() = ShoeListViewModel::class.java
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentShoeDetailBinding = DataBindingUtil.inflate(
        inflater,
        R.layout.fragment_shoe_detail,
        container,
        false
    )
    override fun getFragmentRepository() = ShoeListRepository(shoeDao)

}