package com.example.myapplication.chats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.ChatMessage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatAdapter(private val messages: List<ChatMessage>, private val userId: Int) :
    RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SENT = 1
        private const val VIEW_TYPE_RECEIVED = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].sender == userId) VIEW_TYPE_SENT else VIEW_TYPE_RECEIVED
    }

    class ChatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtContent: TextView = view.findViewById(R.id.txtContent)
        val txtDate: TextView = view.findViewById(R.id.txtDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val layout = if (viewType == VIEW_TYPE_SENT) {
            R.layout.item_chat_message_sent // ✅ New XML for sent messages
        } else {
            R.layout.item_chat_message_received // ✅ Existing XML for received messages
        }
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ChatViewHolder(view)
    }

    fun formatDate(messageDate: Date): String {
        val currentDate = Date()

        // Calculate the difference in milliseconds
        val diff = currentDate.time - messageDate.time
        val diffHours = diff / (1000 * 60 * 60) // Convert milliseconds to hours

        // Choose format based on the time difference
        val dateFormat = if (diffHours < 24) {
            SimpleDateFormat("HH:mm", Locale.getDefault()) // Show only hour and minutes
        } else {
            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()) // Show full date and time
        }

        return dateFormat.format(messageDate)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val message = messages[position]
        holder.txtContent.text = message.content
        holder.txtDate.text = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
    }

    fun scrollToBottom(recyclerView: RecyclerView) {
        if (messages.isNotEmpty()) {
            recyclerView.scrollToPosition(messages.size - 1)
        }
    }


    override fun getItemCount() = messages.size
}
