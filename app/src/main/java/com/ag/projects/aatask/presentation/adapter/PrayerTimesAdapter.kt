package com.ag.projects.aatask.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ag.projects.aatask.databinding.PrayerTimesItemBinding
import com.ag.projects.domain.model.prayer_times.Data

class PrayerTimesAdapter: RecyclerView.Adapter<PrayerTimesAdapter.PrayerTimesViewHolder>() {

    inner class PrayerTimesViewHolder(
        val binding: PrayerTimesItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    private val diffUtil = object : DiffUtil.ItemCallback<Data>() {

        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }

    val syncListDiffer = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrayerTimesViewHolder {
        return PrayerTimesViewHolder(
            PrayerTimesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: PrayerTimesViewHolder, position: Int) {

        val prayerTimeItem = syncListDiffer.currentList[position]
        holder.binding.apply {

            tvFajrTime.text = prayerTimeItem.timings.fajr
            tvSunriseTime.text = prayerTimeItem.timings.sunrise
            tvDuhrTime.text = prayerTimeItem.timings.dhuhr
            tvAsrTime.text = prayerTimeItem.timings.asr
            tvMaghribTime.text = prayerTimeItem.timings.maghrib
            tvIshaTime.text = prayerTimeItem.timings.isha
        }
    }

    override fun getItemCount() = syncListDiffer.currentList.size
}