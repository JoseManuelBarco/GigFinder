package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.objects.Event

class EventAdapter(private val eventList: List<Event>) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nombreTextView: TextView = view.findViewById(R.id.nombreTextView)
        val fechaTextView: TextView = view.findViewById(R.id.fechaTextView)
        val horaTextView: TextView = view.findViewById(R.id.horaTextView)
        val imageView: ImageView = view.findViewById(R.id.imageView) // Asegúrate de tener un ID correcto en XML
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.musician_event_opportunities_item, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val event = eventList[position]
        holder.nombreTextView.text = event.name
        holder.fechaTextView.text = event.date
        holder.horaTextView.text = event.time
        holder.imageView.setImageResource(R.drawable.logogigfinder)
    }

    override fun getItemCount() = eventList.size
}