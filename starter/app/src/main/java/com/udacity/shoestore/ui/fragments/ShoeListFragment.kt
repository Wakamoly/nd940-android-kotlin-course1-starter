package com.udacity.shoestore.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.udacity.shoestore.R
import com.udacity.shoestore.Resource
import com.udacity.shoestore.base.BaseFragment
import com.udacity.shoestore.databinding.FragmentShoeListBinding
import com.udacity.shoestore.db.ShoeStoreDatabase
import com.udacity.shoestore.db.dao.ShoeDao
import com.udacity.shoestore.db.entities.ShoeEntity
import com.udacity.shoestore.handleApiError
import com.udacity.shoestore.repositories.ShoeListRepository
import com.udacity.shoestore.ui.adapters.ShoeListAdapter
import com.udacity.shoestore.ui.adapters.ViewPagerAdapter
import com.udacity.shoestore.ui.fragments.view_models.ShoeListViewModel
import com.udacity.shoestore.visible

class ShoeListFragment : BaseFragment<ShoeListViewModel, FragmentShoeListBinding, ShoeListRepository>() {

    private lateinit var shoeDao: ShoeDao

    /**
     * Not allowed to use recyclerview, must use Scrollview
     * and input data/layouts manually
     *
     */
    //private lateinit var shoeListAdapter: ShoeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCtx = requireContext()
        shoeDao = ShoeStoreDatabase(mCtx).getShoeDao()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
        initRecycler()
    }

    private fun subscribeObservers(){
        viewModel.shoes.observe(viewLifecycleOwner, Observer {
            when(it){
                is Resource.Loading -> loading()
                is Resource.Success -> pushDataToRecycler(it.value)
                is Resource.Failure -> handleApiError(it)
            }
        })
    }

    private fun loading(){
        binding.recyclerProgress.visible(true)
    }

    private fun pushDataToRecycler(entities: List<ShoeEntity>){
        binding.recyclerProgress.visible(false)
        //shoeListAdapter.addItems(entities)
        for (entity in entities){
            val subViewInflater = LayoutInflater.from(mCtx).inflate(R.layout.recycler_shoes, null, false)
            val layout = subViewInflater.findViewById<ConstraintLayout>(R.id.shoe_list_layout)
            val descriptionText = subViewInflater.findViewById<TextView>(R.id.description_tv)
            val viewPager = subViewInflater.findViewById<ViewPager>(R.id.shoe_image_viewpager)
            val sliderDots: TabLayout = subViewInflater.findViewById(R.id.slider_dots)
            sliderDots.setupWithViewPager(viewPager, true)
            descriptionText.text = entity.description
            viewPager.adapter = ViewPagerAdapter(mCtx, entity.images)

            // TODO: 11/30/20 Set onClickListener for "layout"
            binding.scrollLinearLayout.addView(subViewInflater)
        }
    }

    private fun initRecycler(){
        /*binding.shoeListRecycler.setHasFixedSize(true)
        binding.shoeListRecycler.layoutManager = LinearLayoutManager(mCtx)
        shoeListAdapter = ShoeListAdapter(mCtx)
        binding.shoeListRecycler.adapter = shoeListAdapter*/
        viewModel.getShoes()
    }

    override fun getViewModel() = ShoeListViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentShoeListBinding.inflate(inflater, container, false)

    override fun getFragmentRepository() = ShoeListRepository(shoeDao)


}