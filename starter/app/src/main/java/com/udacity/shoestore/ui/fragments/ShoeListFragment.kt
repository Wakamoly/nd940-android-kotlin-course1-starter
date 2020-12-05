package com.udacity.shoestore.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.udacity.shoestore.*
import com.udacity.shoestore.base.BaseFragment
import com.udacity.shoestore.base.ViewModelFactory
import com.udacity.shoestore.databinding.FragmentShoeListBinding
import com.udacity.shoestore.databinding.RecyclerShoesBinding
import com.udacity.shoestore.db.ShoeStoreDatabase
import com.udacity.shoestore.db.dao.ShoeDao
import com.udacity.shoestore.db.entities.ShoeEntity
import com.udacity.shoestore.repositories.ShoeListRepository
import com.udacity.shoestore.ui.activities.MainActivity
import com.udacity.shoestore.ui.adapters.ShoeListAdapter
import com.udacity.shoestore.ui.adapters.ViewPagerAdapter
import com.udacity.shoestore.ui.fragments.view_models.ShoeListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.w3c.dom.Text

class ShoeListFragment : BaseFragment<ShoeListViewModel, FragmentShoeListBinding, ShoeListRepository>() {

    private lateinit var shoeDao: ShoeDao

    /**
     * Not allowed to use Recyclerview, must use Scrollview
     * and input data/layouts manually
     */
    //private lateinit var shoeListAdapter: ShoeListAdapter
    private lateinit var inflaterSL: LayoutInflater
    private lateinit var containerSL: ViewGroup

    /**
     * delegate activityViewModels() as instructed, we could also use the viewModel
     * variable interchangeably due to our usage of BaseFragment<*,*,*>().
     *
     * Using our own ViewModelFactory as we'll need the Repo for DB querying.
     */
    private val shoeViewModel: ShoeListViewModel by activityViewModels { ViewModelFactory(getFragmentRepository()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mCtx = requireContext()
        shoeDao = ShoeStoreDatabase(mCtx).getShoeDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        inflaterSL = inflater
        if (container != null) {
            containerSL = container
        }
        setHasOptionsMenu(true)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.shoe_listings_text)
        subscribeObservers()
        initRecycler()
        binding.addShoeButton.setOnClickListener {
            findNavController().navigate(ShoeListFragmentDirections.actionShoelistDestinationToShoeDetailFragment())
        }
    }

    private fun subscribeObservers(){
        /**
         * By using our Resource sealed class, we're able to observe different data states
         * and update UI accordingly.
         */
        shoeViewModel.shoes.observe(viewLifecycleOwner, Observer {
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
            val shoeBinding = DataBindingUtil.inflate<RecyclerShoesBinding>(inflaterSL, R.layout.recycler_shoes, containerSL, false)

            val shoeSizeText = "Size: US ${entity.size}"
            val shoeCompanyText = "Mfg.: ${entity.company}"
            shoeBinding.shoeTitle.text = entity.name
            shoeBinding.shoeSize.text = shoeSizeText
            shoeBinding.shoeCompany.text = shoeCompanyText
            shoeBinding.sliderDots.setupWithViewPager(shoeBinding.shoeImageViewpager, true)
            shoeBinding.descriptionTv.text = entity.description

            if (entity.images.isEmpty()){
                shoeBinding.shoeImageViewpager.visible(false)
                shoeBinding.sliderDots.visible(false)
            }else {
                shoeBinding.shoeImageViewpager.visible(true)
                shoeBinding.sliderDots.visible(true)
                shoeBinding.shoeImageViewpager.adapter = ViewPagerAdapter(mCtx, entity.images)
            }

            shoeBinding.shoeListLayout.setOnClickListener {
                requireView().findNavController().navigate(ShoeListFragmentDirections.actionShoelistDestinationToShoeDetailFragment(entity))
            }
            binding.scrollLinearLayout.addView(shoeBinding.root)
        }
    }

    private fun initRecycler(){
        /*binding.shoeListRecycler.setHasFixedSize(true)
        binding.shoeListRecycler.layoutManager = LinearLayoutManager(mCtx)
        shoeListAdapter = ShoeListAdapter(mCtx)
        binding.shoeListRecycler.adapter = shoeListAdapter*/
        shoeViewModel.getShoes()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.navdrawer_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout->{
                CoroutineScope(Dispatchers.IO).launch {
                    UserPreferences(mCtx).clear()
                    ShoeStoreDatabase(mCtx).clearAllTables()
                }
                val intent = Intent(activity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                (requireActivity() as AppCompatActivity).finish()
                startActivity(intent)
            }
            R.id.listFragment->{
                findNavController().navigate(R.id.shoelist_destination)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun getViewModel() = ShoeListViewModel::class.java

    /**
     * DataBindingUtil as required.
     */
    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentShoeListBinding = DataBindingUtil.inflate(
        inflater,
        R.layout.fragment_shoe_list,
        container,
        false
    )

    override fun getFragmentRepository() = ShoeListRepository(shoeDao)

}