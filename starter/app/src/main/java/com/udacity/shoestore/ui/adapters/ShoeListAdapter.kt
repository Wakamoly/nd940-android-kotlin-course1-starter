package com.udacity.shoestore.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.udacity.shoestore.R
import com.udacity.shoestore.base.BaseViewHolder
import com.udacity.shoestore.db.entities.ShoeEntity

class ShoeListAdapter(private val context: Context) : RecyclerView.Adapter<BaseViewHolder>() {
    private var isLoaderVisible = false
    private val shoes: MutableList<ShoeEntity> = ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            VIEW_TYPE_NORMAL -> ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.recycler_shoes, parent, false))
            VIEW_TYPE_LOADING -> ProgressHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_progress, parent, false))
            else -> ProgressHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_progress, parent, false))
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        holder.onBind(position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (isLoaderVisible) {
            if (position == shoes.size - 1) VIEW_TYPE_LOADING else VIEW_TYPE_NORMAL
        } else {
            VIEW_TYPE_NORMAL
        }
    }

    override fun getItemCount(): Int {
        return shoes.size
    }

    fun addItems(items: List<ShoeEntity>) {
        shoes.addAll(items)
        notifyItemRangeInserted(items.lastIndex, items.size)
    }

    fun addItemsToTop(items: List<ShoeEntity>) {
        shoes.addAll(0, items)
        notifyItemRangeInserted(0, items.size)
    }

    fun addLoading() {
        isLoaderVisible = true
        shoes.add(ShoeEntity("",0.0,"","", emptyList()))
        notifyItemInserted(shoes.size - 1)
    }

    fun removeLoading() {
        isLoaderVisible = false
        if (shoes.isNotEmpty()) {
            val position = shoes.size - 1
            //val item = getItem(position)
            shoes.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    private fun setAnimation(viewToAnimate: View, position: Int) {
        if (position >= 0) {
            val animation: Animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_left)
            viewToAnimate.startAnimation(animation)
        }
    }

    fun size(): Int {
        return shoes.size
    }

    fun clear() {
        shoes.clear()
        notifyDataSetChanged()
    }

    private fun getItem(position: Int): ShoeEntity {
        return shoes[position]
    }

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {
        //private var nickname: TextView = itemView.findViewById(R.id.notificationsNickname)
        override fun clear() {}
        override fun onBind(position: Int) {
            super.onBind(position)
            val shoe = shoes[position]


            setAnimation(itemView, position)
        }
    }

    class ProgressHolder internal constructor(itemView: View?) : BaseViewHolder(itemView) {
        override fun clear() {}
    }

    companion object {
        private const val VIEW_TYPE_LOADING = 0
        private const val VIEW_TYPE_NORMAL = 1
    }
}