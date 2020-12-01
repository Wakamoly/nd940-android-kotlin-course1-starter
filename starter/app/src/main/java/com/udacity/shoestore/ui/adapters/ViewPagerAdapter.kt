package com.udacity.shoestore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.udacity.shoestore.R
import java.util.*


class ViewPagerAdapter(context: Context, images: List<String>) :
    PagerAdapter() {
    // Context object
    var context: Context = context

    // Array of image strings
    var images: List<String> = images

    // Layout Inflater
    var mLayoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    override fun getCount(): Int {
        // return the number of images
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView: View = mLayoutInflater.inflate(R.layout.image_item, container, false)
        val imageView: ImageView = itemView.findViewById(R.id.view_pager_image) as ImageView

        Glide.with(context)
            .load(images[position])
            .into(imageView)

        // Adding the View
        Objects.requireNonNull(container).addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

}