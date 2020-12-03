package com.udacity.shoestore.ui.adapters

import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.udacity.shoestore.R
import com.udacity.shoestore.databinding.ImageItemBinding
import java.util.*


class ViewPagerAdapter(
    // Context object
    var context: Context,
    // Array of image strings
    var images: List<String>
) : PagerAdapter() {
    var mLayoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as ImageView
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        /**
         * even more view binding
         */
        val imageBinding = DataBindingUtil.inflate<ImageItemBinding>(mLayoutInflater, R.layout.image_item, container, false)

        Glide.with(context)
            .load(images[position])
            .into(imageBinding.viewPagerImage)

        Objects.requireNonNull(container).addView(imageBinding.root)
        return imageBinding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

}