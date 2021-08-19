package com.fit.diplomski.app

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fit.diplomski.app.databinding.TempActivityRecognitionItemBinding
import com.fit.diplomski.app.model.RecognitionLogData

class ActivityRecognitionLogAdapter(val list: ArrayList<RecognitionLogData>)
    : RecyclerView.Adapter<ActivityRecognitionLogAdapter.ViewHolder>()
{

    public fun addNewActivity(recognitionLogData: RecognitionLogData)
    {
        list.add(recognitionLogData)
        notifyItemInserted(list.size - 1)
    }

    inner class ViewHolder(val binding: TempActivityRecognitionItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(TempActivityRecognitionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.timeTextView.text = list[position].time
        holder.binding.activityNameTextView.text = list[position].activityName
    }

    override fun getItemCount(): Int {
        return list.size
    }
}