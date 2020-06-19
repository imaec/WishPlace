package com.imaec.wishplace.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.VideoController
import com.google.android.gms.ads.formats.UnifiedNativeAd
import com.google.android.gms.ads.formats.UnifiedNativeAdView
import com.imaec.wishplace.R
import com.imaec.wishplace.TYPE_AD
import com.imaec.wishplace.TYPE_ITEM
import com.imaec.wishplace.base.BaseAdapter
import com.imaec.wishplace.databinding.ItemItemBinding
import com.imaec.wishplace.model.PlaceDTO
import com.imaec.wishplace.room.entity.PlaceEntity

class ListAdapter : BaseAdapter() {

    override fun getItemViewType(position: Int): Int = when {
        listItem[position] is PlaceDTO -> TYPE_ITEM
        else -> TYPE_AD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_ITEM -> {
                binding = ItemItemBinding.inflate(LayoutInflater.from(parent.context))
                ItemViewHolder(binding as ItemItemBinding)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.ad_unified_home, parent, false)
                AdViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.onBind(listItem[position] as PlaceDTO)
        } else if (holder is AdViewHolder) {
            holder.onBind(listItem[position] as UnifiedNativeAd)
        }
    }

    inner class ItemViewHolder(private val binding: ItemItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(item: PlaceDTO) {
            binding.apply {
                this.item = item
                root.setOnClickListener {
                    onClickWithTransition(item, imageItemThumb)
                }
                root.setOnLongClickListener {
                    onLongClick(item)
                    true
                }
            }
        }
    }

    inner class AdViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val adView by lazy { itemView.findViewById<UnifiedNativeAdView>(R.id.ad_view) }

        fun onBind(nativeAd: UnifiedNativeAd) {
            populateUnifiedNativeAdView(nativeAd, adView)
        }

        private fun populateUnifiedNativeAdView(nativeAd: UnifiedNativeAd, adView: UnifiedNativeAdView) {
            adView.apply {
                mediaView = adView.findViewById(R.id.ad_media)
                headlineView = adView.findViewById(R.id.ad_headline)
                callToActionView = adView.findViewById(R.id.ad_call_to_action)
                iconView = adView.findViewById(R.id.ad_app_icon)
            }

            (adView.headlineView as TextView).text = nativeAd.headline
            adView.mediaView.setMediaContent(nativeAd.mediaContent)
            if (nativeAd.callToAction == null) {
                adView.callToActionView.visibility = View.INVISIBLE
            } else {
                adView.callToActionView.visibility = View.VISIBLE
                (adView.callToActionView as TextView).text = "자세히 알아보기"
            }

            adView.setNativeAd(nativeAd)
        }
    }
}