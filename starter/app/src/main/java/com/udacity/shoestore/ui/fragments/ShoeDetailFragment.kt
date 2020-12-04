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
            Timber.d(shoeEntity.id.toString())

            binding.titleEditText.setText(shoeEntity.name)
            binding.shoeSizeEditText.setText(shoeEntity.size.toString())
            binding.companyEditText.setText(shoeEntity.company)
            binding.descriptionEditText.setText(shoeEntity.description)

            when(shoeEntity.images.size){
                3 -> {
                    binding.urlEt1.setText(shoeEntity.images[0])
                    binding.urlEt2.setText(shoeEntity.images[1])
                    binding.urlEt3.setText(shoeEntity.images[2])
                }
                2 -> {
                    binding.urlEt1.setText(shoeEntity.images[0])
                    binding.urlEt2.setText(shoeEntity.images[1])
                }
                1 -> binding.urlEt1.setText(shoeEntity.images[0])
            }
        }else{
            /**
             * Floating button to add new shoe pressed, not editing a existing listing
             */
            Timber.d("ShoeEntity null!")
            (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.add_shoe_text)
        }
    }

    fun saveShoe(){
        if (binding.shoeSizeEditText.text.toString().toDoubleOrNull() != null){
            val shoeSize = binding.shoeSizeEditText.text.toString().toDouble()
            val title = binding.titleEditText.text.toString()
            val shoeCompany = binding.companyEditText.text.toString()
            val shoeDescription = binding.descriptionEditText.text.toString()
            val imageUrl1 = binding.urlEt1.text.toString()
            val imageUrl2 = binding.urlEt2.text.toString()
            val imageUrl3 = binding.urlEt3.text.toString()
            val imageList: MutableList<String> = ArrayList()
            if (imageUrl1.isNotEmpty()){
                imageList.add(imageUrl1)
            }
            if (imageUrl2.isNotEmpty()){
                imageList.add(imageUrl2)
            }
            if (imageUrl3.isNotEmpty()){
                imageList.add(imageUrl3)
            }
            val newShoeEntity = ShoeEntity(title,shoeSize,shoeCompany,shoeDescription,imageList)
            if (::shoeEntity.isInitialized){
                shoeViewModel.updateShoe(newShoeEntity, shoeEntity.id)
            }else{
                shoeViewModel.saveShoe(newShoeEntity)
            }
        }else{
            requireView().snackbar("Shoe Size must be in Double format! (Ex. \"9.5\")")
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